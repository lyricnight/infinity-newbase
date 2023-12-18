package club.lyric.infinity.api.event.mc.update;


import me.bush.eventbus.event.Event;

public class UpdateWalkingPlayerEvent extends Event {
    private final int stage;
    @Override
    protected boolean isCancellable() {
        return true;
    }

    public UpdateWalkingPlayerEvent(int stage)
    {
        this.stage = stage;
    }

    public int getStage()
    {
        return stage;
    }
}