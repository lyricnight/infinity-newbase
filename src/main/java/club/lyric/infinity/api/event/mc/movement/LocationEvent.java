package club.lyric.infinity.api.event.mc.movement;

import club.lyric.infinity.api.event.Event;
import net.minecraft.util.math.Vec3d;

/**
 * @author lyric
 * fired whenever player movement/rotation is sent to server.
 */
public class LocationEvent extends Event {

    private double x;

    private double y;

    private double z;

    private float yaw;

    private float pitch;

    private boolean onGround;

    private boolean modified;

    public LocationEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public static class Pre extends LocationEvent {
        public Pre(double x, double y, double z, float yaw, float pitch, boolean onGround)
        {
            super(x, y, z, yaw, pitch, onGround);
        }
    }

    public static class Post extends LocationEvent {
        public Post(double x, double y, double z, float yaw, float pitch, boolean onGround)
        {
            super(x, y, z, yaw, pitch, onGround);
        }
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setYaw(float yaw) {
        modified = true;
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        modified = true;
        this.pitch = pitch;
    }

    public void setOnGround(boolean onGround) {
        modified = true;
        this.onGround = onGround;
    }
}
