package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.movement.NoJumpDelay;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Unique
    @Shadow
    private int jumpTicks;
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(final CallbackInfo ci) {
        final NoJumpDelay NO_JUMP_DELAY = Managers.MODULES.getModuleFromClass(NoJumpDelay.class);
        if (NO_JUMP_DELAY.isOn()) {
            jumpTicks = 0;
        }
    }
}