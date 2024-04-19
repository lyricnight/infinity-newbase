package club.lyric.infinity.api.event.bus;

import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author railhack
 */
public final class RegistrableTarget {
    private final Class<?> targetClass;
    private final AccessType accessType;

    public RegistrableTarget(Object target) {
        this.targetClass = target instanceof Class<?> ? (Class<?>) target : target.getClass();
        this.accessType = target instanceof Class<?> ? AccessType.STATIC : AccessType.VIRTUAL;
    }

    public Method[] getDeclaredMethods() {
        return switch (this.accessType) {
            case STATIC -> Arrays.stream(this.targetClass.getDeclaredMethods())
                    .filter(m -> (m.getModifiers() & Opcodes.ACC_STATIC) != 0)
                    .toArray(Method[]::new);
            case VIRTUAL -> Arrays.stream(this.targetClass.getDeclaredMethods())
                    .filter(m -> (m.getModifiers() & Opcodes.ACC_STATIC) == 0)
                    .toArray(Method[]::new);
        };
    }

    public MethodType retrieveInvoker() {
        return switch (this.accessType) {
            case STATIC -> MethodType.methodType(Invoker.class);
            case VIRTUAL -> MethodType.methodType(Invoker.class, this.getTargetClass());
        };
    }

    public MethodHandle retrieveHandle(MethodHandles.Lookup lookup, Method method) throws NoSuchMethodException, IllegalAccessException {
        return switch (this.accessType) {
            case STATIC ->
                    lookup.findStatic(this.targetClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameters()[0].getType()));
            case VIRTUAL ->
                    lookup.findVirtual(this.targetClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameters()[0].getType()));
        };
    }

    public Invoker generateInvoker(MethodHandle targetHandle, Object instance) throws Throwable {
        return switch (this.accessType) {
            case STATIC -> (Invoker) targetHandle.invokeExact();
            case VIRTUAL -> (Invoker) targetHandle.invoke(instance);
        };
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public AccessType getAccessType() {
        return this.accessType;
    }

    public enum AccessType {
        STATIC,
        VIRTUAL
    }
}