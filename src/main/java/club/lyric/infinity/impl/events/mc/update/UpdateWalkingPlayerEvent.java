package club.lyric.infinity.impl.events.mc.update;


import club.lyric.infinity.api.event.Event;
import lombok.Getter;

/**
 * @author vasler
 */
@Getter
public class UpdateWalkingPlayerEvent extends Event {
    private final int stage;

    public UpdateWalkingPlayerEvent(int stage) {
        this.stage = stage;
    }

}