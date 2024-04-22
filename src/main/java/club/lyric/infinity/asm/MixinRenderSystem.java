package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.client.gui.IMLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(RenderSystem.class)
public class MixinRenderSystem {
    @Inject(at = @At("HEAD"), method = "flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("ImGui Render");
        IMLoader.onFrameRender();
        MinecraftClient.getInstance().getProfiler().pop();
    }
}
