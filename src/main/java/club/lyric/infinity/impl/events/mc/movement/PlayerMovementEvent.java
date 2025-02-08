package club.lyric.infinity.impl.events.mc.movement;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

/**
 * @author lyric
 */
@Getter
@Setter
public class PlayerMovementEvent extends Event {
    private final MovementType type;
    private double x, y, z;

    public PlayerMovementEvent(MovementType type, Vec3d movement) {
        this.type = type;
        this.x = movement.getX();
        this.y = movement.getY();
        this.z = movement.getZ();
    }

    public Vec3d getMovement() {
        return new Vec3d(x, y, z);
    }
}
