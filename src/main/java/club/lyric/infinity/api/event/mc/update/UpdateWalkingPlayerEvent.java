package club.lyric.infinity.api.event.mc.update;


import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 */
public class UpdateWalkingPlayerEvent extends Event {
    private final int stage;

    public UpdateWalkingPlayerEvent(int stage)
    {
        this.stage = stage;
    }

    public int getStage()
    {
        return stage;
    }
}