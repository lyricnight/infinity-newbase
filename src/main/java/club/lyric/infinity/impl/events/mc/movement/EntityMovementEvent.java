package club.lyric.infinity.impl.events.mc.movement;

import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.MovementType;

/**
 * @author lyric
 * event fired when we move.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EntityMovementEvent extends Event {
    private final MovementType type;

    private double x;

    private double y;

    private double z;
}
