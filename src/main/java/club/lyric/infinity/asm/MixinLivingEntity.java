package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.impl.events.render.InterpolationEvent;
import club.lyric.infinity.api.util.client.math.Time;
import club.lyric.infinity.impl.modules.movement.NoJumpDelay;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author vasler
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Unique
    private long lastInterp = 0L;
    @Shadow
    private int jumpingCooldown;

    @Inject(method = "updateTrackedPositionAndAngles", at = @At(value = "HEAD"))
    private void updateTrackedPositionAndAnglesHead(double x, double y, double z, float yaw, float pitch, int interpolationSteps, CallbackInfo ci) {
        EventBus.getInstance().post(new InterpolationEvent(LivingEntity.class.cast(this), x, y, z, yaw, pitch, lastInterp));
        lastInterp = Time.getMillis();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(final CallbackInfo ci) {
        final NoJumpDelay NO_JUMP_DELAY = Managers.MODULES.getModuleFromClass(NoJumpDelay.class);
        if (NO_JUMP_DELAY.isOn()) {
            jumpingCooldown = 0;
        }
    }
}