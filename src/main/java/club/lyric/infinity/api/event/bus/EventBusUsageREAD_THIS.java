package club.lyric.infinity.api.event.bus;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.bus.Invoker;
import club.lyric.infinity.api.event.bus.RegistrableTarget;

public class EventBusUsageREAD_THIS {

    /**
     * {@link EventBus ::register()} use like EventBus.getInstance().register(this) - you can also directly register a class (instead of this, Module.class)
     * {@link EventBus::unregister()} use like EventBus.getInstance().register(this) - you can also directly unregister a class (instead of this, Module.class)
     * {@link EventBus::post()} use like EventBus.getInstance().post(event)
     * {@link EventHandler} goes above a method to receive events, example:
     * @EventHandler
     * public void receiveEvent(Event event) {
     *     // do something here
     * }
     * can also do @EventHandler(priority = 9999)
     * {@link Invoker} don't use this
     * {@link RegistrableTarget} don't use this
     */
}