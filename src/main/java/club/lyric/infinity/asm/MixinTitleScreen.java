package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vasler
 */
@Mixin(value = TitleScreen.class)
public abstract class MixinTitleScreen {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", ordinal = 0))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        Managers.TEXT.drawString(Infinity.CLIENT_NAME + Infinity.VERSION, 2, 2, (Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(2) : Managers.MODULES.getModuleFromClass(Colours.class).getColor()).getRGB());

        Managers.TEXT.drawString(Formatting.GRAY + " build (" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ")", Managers.TEXT.width(Infinity.CLIENT_NAME + Infinity.VERSION, true) + 2, 2, -1);

        Render2DUtils.drawRoundedRect(context.getMatrices(), 2, 2, 30, 30, 6, -1);
    }
}
