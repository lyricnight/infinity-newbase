package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IDrawContext;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

/**
 * @author lyric
 * for rendering our own text whenever we want using a util, like we can do in 1.12.2.
 * This is probably much more complicated than just rendering in render methods, but we need it for things like main menu text rendering.
 * 1.20 requires a context per-tick, so we make our own, but doesn't let us use float x and y values, even though it literally casts them to float anyway???
 * so we make our own method, that we inject into the target class.
 */
@Mixin(DrawContext.class)
public abstract class MixinDrawContext implements IDrawContext {
    @Final
    @Shadow
    private MatrixStack matrices;

    @Final
    @Shadow
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    @Deprecated
    protected abstract void tryDraw();

    @Override
    public float infinity_newbase$drawText(TextRenderer renderer, @Nullable String text, float x, float y, int color, boolean shadow) {
        float i;
        if (text == null) {
            return 0f;
        } else {

            if (!shadow)
            {
                int shadowColor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
                renderer.draw(text, x + 0.5f, y + 0.5f, color, true, this.matrices.peek().getPositionMatrix(), this.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0, renderer.isRightToLeft());
            }

            i = renderer.draw(text, x, y, color, shadow, this.matrices.peek().getPositionMatrix(), this.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880, renderer.isRightToLeft());
            this.tryDraw();
            return i;
        }
    }
}
