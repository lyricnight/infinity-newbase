package club.lyric.infinity.asm;

import club.lyric.infinity.api.gui.IMLoader;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(RenderSystem.class)
public class MixinRenderSystem implements IMinecraft {
    @Inject(at = @At("HEAD"), method = "flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        mc.getProfiler().push("ImGui Render");
        IMLoader.onFrameRender();
        mc.getProfiler().pop();
    }
}
