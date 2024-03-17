package club.lyric.infinity.manager.font;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.render.font.FontRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FontManager {
    public MatrixStack matrix;
    public FontRenderer fontRenderer = new FontRenderer(Infinity.class.getClassLoader().getResourceAsStream("assets/JetBrainsMono-Regular.ttf"), 18);

    public MatrixStack getMatrixStack() {
        return this.matrix;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrix = matrixStack;
    }

}