package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.ducks.IDrawContext;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

/**
 * @author lyric
 * I spent 2 hours reading minecraft code to figure out how to instantiate DrawContext...
 */

//TODO: once we add custom fonts, make them render through this.
public class TextManager implements IMinecraft {

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
        if(!ready) return;
        ((IDrawContext)context).infinity_newbase$drawText(mc.textRenderer, value, x, y, color, shadow);
    }

    public float width(TextRenderer renderer, String value, boolean shadow)
    {
        if(!ready) return 0f;
        return renderer.getWidth(value) + (shadow ? 1 : 0);
    }

    public float height(TextRenderer renderer, boolean shadow)
    {
        if(!ready) return 0f;
        return renderer.fontHeight + (shadow ? 1 : 0);
    }
}
