package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.ducks.IDrawContext;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author lyric
 * I spent 2 hours reading minecraft code to figure out how to instantiate DrawContext...
 */

//TODO: once we add custom fonts, make them render through this.
public final class TextManager implements IMinecraft {
    public MatrixStack matrix;

    /**
     * thing that does everything.
     */
    private DrawContext context;

    private boolean ready;

    public void init()
    {
        context = new DrawContext(mc, mc.getBufferBuilders().getEntityVertexConsumers());
        ready = true;
    }

    public void drawString(String value, float x, float y, int color, boolean shadow)
    {
        if(!ready)
        {
            Infinity.LOGGER.error("drawString() called when null.");
            return;
        }
        ((IDrawContext)context).infinity_newbase$drawText(mc.textRenderer, value, x, y, color, shadow);
    }

    public float width(String value, boolean shadow)
    {
        if(!ready)
        {
            Infinity.LOGGER.error("width() called when null.");
            return 0f;
        }
        return mc.textRenderer.getWidth(value) + (shadow ? 1 : 0);
    }

    public float height(boolean shadow)
    {
        if(!ready)
        {
            Infinity.LOGGER.error("height() called when null.");
            return 0f;
        }
        return mc.textRenderer.fontHeight + (shadow ? 1 : 0);
    }

    public MatrixStack getMatrixStack() {
        return this.matrix;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrix = matrixStack;
    }
}
