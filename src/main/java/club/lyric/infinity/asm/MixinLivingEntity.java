package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.client.math.Time;
import club.lyric.infinity.impl.events.render.InterpolationEvent;
import club.lyric.infinity.impl.modules.movement.Step;
import club.lyric.infinity.impl.modules.player.AntiLevitation;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author lyric
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Unique
    private long lastInterp = 0L;

    @Inject(method = "updateTrackedPositionAndAngles", at = @At(value = "HEAD"))
    private void updateTrackedPositionAndAnglesHead(double x, double y, double z, float yaw, float pitch, int interpolationSteps, CallbackInfo ci) {
        EventBus.getInstance().post(new InterpolationEvent(LivingEntity.class.cast(this), x, y, z, yaw, pitch, lastInterp));
        lastInterp = Time.getMillis();
    }

    @Inject(at = @At("HEAD"), method = "hasStatusEffect", cancellable = true)
    private void hasStatusEffect(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect.equals(StatusEffects.LEVITATION) && Managers.MODULES.getModuleFromClass(AntiLevitation.class).isOn()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * @author lyric
     * @reason .../.
     */
    @Overwrite
    public float getStepHeight()
    {
        if (Managers.MODULES.getModuleFromClass(Step.class).isOn())
        {
            return Managers.MODULES.getModuleFromClass(Step.class).height.getFValue();
        }
        else return 0.6f;
    }
}