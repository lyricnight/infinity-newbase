package club.lyric.infinity.api.event.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.LivingEntity;

/**
 * @author lyric
 * event fired whenever an entity is interpolated client-side
 */
public final class InterpolationEvent extends Event {
    private final LivingEntity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float yRot;
    private final float xRot;
    private final long lastInterp;

    public InterpolationEvent(LivingEntity entity, double x, double y, double z, float yRot, float xRot, long lastInterp)
    {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yRot = yRot;
        this.xRot = xRot;
        this.lastInterp = lastInterp;
    }

    public LivingEntity getEntity() {
        return entity;
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

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public long getLastInterp() {
        return lastInterp;
    }
}
