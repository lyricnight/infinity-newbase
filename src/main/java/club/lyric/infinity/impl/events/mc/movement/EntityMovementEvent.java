package club.lyric.infinity.impl.events.mc.movement;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.MovementType;

/**
 * @author lyric
 * event fired when we move.
 */
@Getter @Setter
public class EntityMovementEvent extends Event {

    private final MovementType type;

    private double x;

    private double y;

    private double z;

    public EntityMovementEvent(MovementType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
