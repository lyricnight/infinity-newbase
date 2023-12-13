package club.lyric.infinity.api.util.render.text;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * @author vasler
 * to make it easier
 */
public class TextUtils implements IMinecraft {

    public static void drawStringWithShadow(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color) {
        context.drawText(textRenderer, text, x, y, color, true);
    }

    public static void drawString(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color) {
        context.drawText(textRenderer, text, x, y, color, false);
    }
}
