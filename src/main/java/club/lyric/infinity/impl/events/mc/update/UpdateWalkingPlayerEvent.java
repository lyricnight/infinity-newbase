package club.lyric.infinity.impl.events.mc.update;


import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vasler
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UpdateWalkingPlayerEvent extends Event {
    private final int stage;
}