package club.lyric.infinity.api.event.bus;

import club.lyric.infinity.api.event.Event;

import java.lang.reflect.Method;

@FunctionalInterface
public interface Invoker {

    void invoke(Event event);

    final class MethodInvoker {
        private final Method method;
        private final Invoker invoker;

        public MethodInvoker(Method method, Invoker invoker) {
            this.method = method;
            this.invoker = invoker;
        }

        public Method getMethod() {
            return this.method;
        }

        public Invoker getInvoker() {
            return this.invoker;
        }

        public int getPriority() {
            return this.method.getAnnotation(EventHandler.class).priority().getValue();
        }
    }
}
