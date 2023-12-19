package club.lyric.infinity.api.event.mc;

import me.lyric.eventbus.event.Event;

/**
 * @author lyric
 */
public class TickEvent extends Event {

    private int stage;


    public TickEvent(int stage)
    {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    @Override
    protected boolean isCancellable() {
        return false;
    }
}
