package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer implements IMinecraft {

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void hookRenderWorld(CallbackInfo ci) {
        if (!Infinity.first) {
            ChatUtils.sendMessagePrivateColored(Formatting.WHITE + "You are currently playing as: " + Formatting.GRAY + mc.player.getName().getString());
            ChatUtils.sendMessagePrivateColored(Formatting.WHITE + "The default prefix for commands is: " + Formatting.RESET + "-");
            ChatUtils.sendMessagePrivateColored(Formatting.WHITE + "The default UI bind is: " + Formatting.RESET + "RSHIFT");
            Infinity.first = true;
        }
    }
}