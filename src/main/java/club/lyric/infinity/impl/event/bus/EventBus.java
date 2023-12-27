package club.lyric.infinity.impl.event.bus;

import club.lyric.infinity.Infinity;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus {

    private static final EventBus instance = new EventBus();

    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private final List<Invoker.MethodInvoker> registeredMethodInvokers = new CopyOnWriteArrayList<>();

    private EventBus() {
    }

    public void register(Object target) {
        try {
            RegistrableTarget registrableTarget = new RegistrableTarget(target);
            for (Method method : registrableTarget.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1) {
                    Parameter parameter = method.getParameters()[0];
                    if (Event.class.isAssignableFrom(parameter.getType())) {
                        MethodType genericVoid = MethodType.methodType(void.class, Event.class);
                        MethodType strictVoid = MethodType.methodType(void.class, parameter.getType());
                        MethodType invokerType = registrableTarget.retrieveInvoker();
                        MethodHandle handle = registrableTarget.retrieveHandle(this.lookup, method);

                        CallSite callSite = LambdaMetafactory.metafactory(
                                this.lookup,
                                Invoker.class.getDeclaredMethods()[0].getName(), // obfuscation
                                invokerType,
                                genericVoid,
                                handle,
                                strictVoid
                        );

                        MethodHandle targetHandle = callSite.getTarget();
                        Invoker invoker = registrableTarget.generateInvoker(targetHandle, target);
                        Invoker.MethodInvoker methodInvoker = new Invoker.MethodInvoker(method, invoker);

                        this.insertOrdinally(methodInvoker);
                    }
                }
            }
        } catch (Throwable t) {
            Infinity.LOGGER.error("Failed to register " + target + ": ", t);
        }
    }

    public void unregister(Object target) {
        RegistrableTarget registrableTarget = new RegistrableTarget(target);
        for (Method method : registrableTarget.getDeclaredMethods()) {
            this.registeredMethodInvokers.removeIf(methodInvoker -> methodInvoker.getMethod().equals(method));
        }
    }

    public void post(Event event) {
        for (Invoker.MethodInvoker methodInvoker : this.registeredMethodInvokers) {
            Method method = methodInvoker.getMethod();
            if (event.getClass().isAssignableFrom(method.getParameters()[0].getType())) {
                try {
                    methodInvoker.getInvoker().invoke(event);
                } catch (Throwable t) {
                    Infinity.LOGGER.error("Failed invoking event: ", t);
                }
            }
        }
    }

    private void insertOrdinally(Invoker.MethodInvoker methodInvoker) {
        int index = 0;
        int priority = methodInvoker.getPriority();
        while (index < this.registeredMethodInvokers.size()) {
            int currPriority = this.registeredMethodInvokers.get(index).getPriority();
            if (currPriority <= priority) {
                break;
            }
            index++;
        }
        this.registeredMethodInvokers.add(index, methodInvoker);
    }

    public static EventBus getInstance() {
        return instance;
    }
}