package club.lyric.infinity.impl.events.mc.movement;

import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 * fired whenever player movement/rotation is sent to server.
 */
public class LocationEvent extends Event {

    private final double x;

    private final double y;

    private final double z;

    private float yaw;

    private float pitch;

    private boolean onGround;

    private boolean modified;

    private boolean horizontal;

    public LocationEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.horizontal = horizontal;
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

    public boolean isModified() {
        return modified;
    }

    public boolean getHorizontal() {
        return horizontal;
    }
}
