package club.lyric.infinity.impl.events.mc;


import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 */
public class TickEvent extends Event {
    public TickEvent() {}

    public static class Pre extends TickEvent {}

    public static class Post extends TickEvent {}
}
