package club.lyric.infinity.api.util.text;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * @author vasler
 * to make it easier 
 */
public class TextUtils implements IMinecraft {

    private static DrawContext context;

    public TextUtils(DrawContext context) {
        TextUtils.context = context;
    }

    public static DrawContext getContext() {
        return context;
    }

    public static void drawStringWithShadow(TextRenderer textRenderer, Text text, int x, int y, int color) {
        getContext().drawText(textRenderer, text, x, y, color, true);
    }

    public static void drawString(TextRenderer textRenderer, Text text, int x, int y, int color) {
        getContext().drawText(textRenderer, text, x, y, color, false);
    }
}
