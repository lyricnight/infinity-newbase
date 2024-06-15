package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.ducks.IDrawContext;
import club.lyric.infinity.api.util.client.render.util.nvg.NVGRenderer;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Fonts;
import club.lyric.infinity.impl.modules.client.HUD;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

/**
 * @author lyric
 * I spent 2 hours reading minecraft code to figure out how to instantiate DrawContext
 * this allows us to draw text whenever we like -> rather than drawing it through getting the drawContext from an event, which means things like drawing text in the server screen become impossible.
 */
public final class TextManager implements IMinecraft {

    /**
     * thing that does everything.
     */
    private DrawContext context;

    private boolean ready;
    public static final NVGRenderer nvgRenderer = new NVGRenderer();

    public void init()
    {
        context = new DrawContext(mc, mc.getBufferBuilders().getEntityVertexConsumers());
        ready = true;
    }

    public void drawString(String value, float x, float y, int color) {
        if (!ready) {
            throw new RuntimeException("drawString() called too early! Report this!");
        }

        if (nvgRenderer.isInitialized() && Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            nvgRenderer.startDraw();
            nvgRenderer.drawText(value, x, y, new Color(color), true);
            nvgRenderer.endDraw();
        }
        else if (Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            nvgRenderer.initialize();
        }
        else
        {
            ((IDrawContext) context).infinity_newbase$drawText(mc.textRenderer, value, x, y, color, Managers.MODULES.getModuleFromClass(HUD.class).shadow.value());
        }
    }

    public float width(String value, boolean shadow)
    {
        if (!ready)
        {
            throw new RuntimeException("width() called too early! Report this!");
        }

        if (nvgRenderer.isInitialized() && Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            return nvgRenderer.getWidth(value);
        }
        else if (Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            nvgRenderer.initialize();
        }

        return mc.textRenderer.getWidth(value) + (shadow ? 1 : 0);
    }

    public int height(boolean shadow)
    {
        if(!ready)
        {
            throw new RuntimeException("height() called too early! Report this!");
        }

        if (nvgRenderer.isInitialized() && Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            return (int) nvgRenderer.getFontHeight();
        }
        else if (Managers.MODULES.getModuleFromClass(Fonts.class).isOn())
        {
            nvgRenderer.initialize();
        }

        return mc.textRenderer.fontHeight + (shadow ? 1 : 0);
    }
}
