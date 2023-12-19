package club.lyric.infinity.api.event.mc.update;


import me.lyric.eventbus.event.Event;

public class UpdateEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return true;
    }
}