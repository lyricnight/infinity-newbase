package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.ducks.IDrawContext;
import club.lyric.infinity.api.util.client.render.text.custom.Font;
import club.lyric.infinity.api.util.client.render.text.custom.GlyphPage;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.HUD;
import club.lyric.infinity.manager.Managers;
import com.google.common.base.Preconditions;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.io.InputStream;

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
    public static Font fontRenderer;

    public void init()
    {
        context = new DrawContext(mc, mc.getBufferBuilders().getEntityVertexConsumers());
        ready = true;
    }

    public void drawString(String value, float x, float y, int color) {
        if (!ready) {
            throw new RuntimeException("drawString() called too early! Report this!");
        }
        else
        {
            ((IDrawContext) context).infinity_newbase$drawText(mc.textRenderer, value, x, y, color, Managers.MODULES.getModuleFromClass(HUD.class).shadow.value());
            fontRenderer.draw(context.getMatrices(), value, x, y, color, true);
        }
    }

    public float width(String value, boolean shadow)
    {
        if (!ready)
        {
            throw new RuntimeException("width() called too early! Report this!");
        }
        return mc.textRenderer.getWidth(value) + (shadow ? 1 : 0);
    }

    public int height(boolean shadow) {
        if (!ready) {
            throw new RuntimeException("height() called too early! Report this!");
        }
        return mc.textRenderer.fontHeight + (shadow ? 1 : 0);
    }

    public static Font create(String file, float size) {

        java.awt.Font font = null;

        try {
            InputStream in = Preconditions.checkNotNull(Font.class.getResourceAsStream("/assets/" + file), "Font resource is null");
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, in)
                    .deriveFont(java.awt.Font.PLAIN, size);
        } catch (Exception ignored) {
        }

        GlyphPage regularPage = new GlyphPage(font, true, true);
        regularPage.generateGlyphPage();
        regularPage.setupTexture();
        return fontRenderer = new Font(regularPage);
    }

}
