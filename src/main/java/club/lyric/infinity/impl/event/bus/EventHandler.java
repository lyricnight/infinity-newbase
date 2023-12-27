package club.lyric.infinity.impl.event.bus;

public @interface EventHandler {

    int priority() default 1000;
}