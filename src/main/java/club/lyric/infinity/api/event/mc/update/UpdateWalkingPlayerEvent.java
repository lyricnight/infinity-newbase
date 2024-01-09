package club.lyric.infinity.api.event.mc.update;


import club.lyric.infinity.api.event.Event;

/**
 * @author vasler
 * why do we use this? its literally useless
 * event's fired whenever player ticks (literally onUpdate) or when movement packets are sent (same thing as motionevent)
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