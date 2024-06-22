package club.lyric.infinity.api.util.minecraft.rotation;

/**
 * @author lyric
 * class that represents a rotation to a point.
 */
public final class RotationPoint {
    private float yaw, pitch;

    /**
     * some sort of priority system that we sort by?
     * could be useful for stricter servers
     */
    private int priority;

    /**
     * will the rotation be instantaneous?
     */
    private boolean instant;

    public RotationPoint(float yaw, float pitch, int priority, boolean instant)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.priority = priority;
        this.instant = instant;
    }

    /**
     * getters
     */

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getInstant() {
        return instant;
    }

    /**
     * setters
     */

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setInstant(boolean instant) {
        this.instant = instant;
    }
}
