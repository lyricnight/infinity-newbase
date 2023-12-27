package club.lyric.infinity.impl.event.bus;

import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;

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
        switch (this.accessType) {
            case STATIC:
                return MethodType.methodType(Invoker.class);
            case VIRTUAL:
                return MethodType.methodType(Invoker.class, this.getTargetClass());
        }
        throw new IllegalStateException("No access type set.");
    }

    public MethodHandle retrieveHandle(MethodHandles.Lookup lookup, Method method) throws NoSuchMethodException, IllegalAccessException {
        switch (this.accessType) {
            case STATIC:
                return lookup.findStatic(this.targetClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameters()[0].getType()));
            case VIRTUAL:
                return lookup.findVirtual(this.targetClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameters()[0].getType()));
        }
        throw new IllegalStateException("No access type set.");
    }

    public Invoker generateInvoker(MethodHandle targetHandle, Object instance) throws Throwable {
        switch (this.accessType) {
            case STATIC:
                return (Invoker) targetHandle.invokeExact();
            case VIRTUAL:
                return (Invoker) targetHandle.invoke(instance);
        }
        throw new IllegalStateException("No access type set.");
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