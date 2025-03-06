package club.lyric.infinity.api.util.minecraft.rotation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lyric
 * class that represents a rotation to a point.
 * TODO remove
 */
@Getter
@Setter
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
    @Getter
    private boolean instant;

    public RotationPoint(float yaw, float pitch, int priority, boolean instant) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.priority = priority;
        this.instant = instant;
    }
}
