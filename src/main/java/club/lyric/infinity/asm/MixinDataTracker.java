package club.lyric.infinity.asm;

import club.lyric.infinity.asm.accessors.ILivingEntity;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 * @param <T> -> type of entity
 */
@Mixin(DataTracker.class)
public class MixinDataTracker<T> {
    @Shadow
    @Final
    private Entity trackedEntity;
    @Inject(method = "set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/data/DataTracker;dirty:Z"))
    public void setHook(TrackedData<T> key, T value, boolean b1, CallbackInfo ci)
    {
        if (ILivingEntity.getHealthId().equals(key) && trackedEntity instanceof PlayerEntity && value instanceof Float num && num <= 0.0f)
        {
            Managers.OTHER.onDeath((PlayerEntity) trackedEntity);
        }
    }
}
