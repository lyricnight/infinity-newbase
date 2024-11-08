package club.lyric.infinity.impl.events.mc.movement;

import club.lyric.infinity.api.event.Event;
import club.lyric.infinity.api.ducks.IVec3d;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

/**
 * @author lyric
 * laziness event made for when we need to distinguish between Pre- and Post-movement.
 */
public class SpecificMovementEvent extends Event {
    public static class Post extends SpecificMovementEvent {}
    public static class Pre extends SpecificMovementEvent {
        public Vec3d og;
        public Vec3d actual;

        public MovementType type;
        public Pre(Vec3d movement, MovementType type)
        {
            this.actual = movement;
            this.type = type;
            this.og = movement;
        }

        public void set(double x, double y, double z)
        {
            ((IVec3d)actual).infinity$set(x, y, z);
        }

        public void setXZ(double x, double z)
        {
            ((IVec3d) actual).infinity$setXZ(x, z);
        }

        public void setY(double y)
        {
            ((IVec3d) actual).infinity$setY(y);
        }
    }
}
