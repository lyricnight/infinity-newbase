package club.lyric.infinity.api.event.mc;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.MovementType;

/**
 * @author lyric
 * event fired when any player moves.
 */
public class EntityMovementEvent extends Event {

    private MovementType type;

    private double x;

    private double y;

    private double z;

    public EntityMovementEvent(MovementType type, double x, double y, double z)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public MovementType getType() {
        return type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
