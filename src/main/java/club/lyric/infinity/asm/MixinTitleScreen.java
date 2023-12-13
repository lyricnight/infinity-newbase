package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.api.util.text.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TitleScreen.class)
public class MixinTitleScreen implements IMinecraft {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", ordinal = 0))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextUtils.drawStringWithShadow(mc.textRenderer, Text.of("t.me/dotgod.cc"), 2, 2, -1);
    }
}
