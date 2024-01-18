package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IDrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
        if (text == null) {
            return 0f;
        } else {
            float i = renderer.draw(text, x, y, color, shadow, this.matrices.peek().getPositionMatrix(), this.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880, renderer.isRightToLeft());
            this.tryDraw();
            return i;
        }
    }
}
