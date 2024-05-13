package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.visual.CameraClip;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class MixinCamera {
    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (Managers.MODULES.getModuleFromClass(CameraClip.class).isOn())
        {
            info.setReturnValue(5.0);
        }
    }
}