package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import club.lyric.infinity.impl.modules.exploit.Interpolation;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;

/**
 * @author lyric
 * event fired whenever an entity is interpolated client-side
 * @see Interpolation ;
 */
@Getter
public final class InterpolationEvent extends Event {
    private final LivingEntity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float yRot;
    private final float xRot;
    private final long lastInterp;

    public InterpolationEvent(LivingEntity entity, double x, double y, double z, float yRot, float xRot, long lastInterp) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yRot = yRot;
        this.xRot = xRot;
        this.lastInterp = lastInterp;
    }

}
