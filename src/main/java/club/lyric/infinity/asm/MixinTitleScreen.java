package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.render.Render2DUtils;
import club.lyric.infinity.api.util.render.text.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static club.lyric.infinity.api.util.minecraft.IMinecraft.mc;

@Mixin(value={TitleScreen.class})
public abstract class MixinTitleScreen {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", ordinal = 0))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        TextUtils.drawStringWithShadow(context, mc.textRenderer, Text.of("t.me/dotgod.cc"), 2, 2, -1);

        Render2DUtils.drawRect(context.getMatrices(), 20, 20, 20, 20, -3977919);
    }
}
