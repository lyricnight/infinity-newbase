package club.lyric.infinity.api.event.mc.update;

import me.bush.eventbus.event.Event;

public class UpdateEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return true;
    }
}