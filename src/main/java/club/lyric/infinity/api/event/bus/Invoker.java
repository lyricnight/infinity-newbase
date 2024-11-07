package club.lyric.infinity.api.event.bus;

import club.lyric.infinity.api.event.Event;

import java.lang.reflect.Method;

/**
 * @author railhack and lyric
 */
@FunctionalInterface
public interface Invoker {
    void invoke(Event event);
    /**
     * testing converting MethodInvoker into a record for higher speed - lyric
     * @param method - method to invoke
     * @param invoker - instance of Invoker
     */
    record MethodInvoker(Method method, Invoker invoker) {
        public int getPriority() {
            return this.method.getAnnotation(EventHandler.class).priority();
        }
    }
}
