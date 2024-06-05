package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.movement.SafeWalk;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    @Inject(method = "clipAtLedge()Z", at = @At("HEAD"), cancellable = true)
    private void clipAtLedge(CallbackInfoReturnable<Boolean> cir) {
         if (Managers.MODULES.getModuleFromClass(SafeWalk.class).isOn() && Managers.MODULES.getModuleFromClass(SafeWalk.class).mode.is("Normal"))
         {
             cir.setReturnValue(true);
         }
    }

}