package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.visual.Ambience;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void getCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }
}