package club.lyric.infinity.api.util.client.render.colors;

import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.manager.Managers;

import java.awt.*;
import java.io.Serial;

/**
 * @author ??
 */
public class JColor extends Color {
    @Serial
    private static final long serialVersionUID = 1L;
    public JColor(int rgb) {
        super(rgb);
    }

    public static JColor getGuiColor() {
        if (Managers.MODULES.getModuleFromClass(ClickGUI.class) == null) {
            throw new RuntimeException("Null check failed! ClickGui is null!");
        }
        return Managers.MODULES.getModuleFromClass(ClickGUI.class).color.getValue();
    }

    public JColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public JColor(int r, int g, int b) {
        super(r, g, b);
    }

    public JColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public JColor(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public JColor(float r, float g, float b) {
        super(r, g, b, 1.0f);
    }

    public JColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public JColor(JColor color, int a) {
        super(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    public static JColor fromHSB(float hue, float saturation, float brightness) {
        return new JColor(Color.getHSBColor(hue, saturation, brightness));
    }

    public JColor jDarker() {
        return new JColor(this.darker());
    }

    public JColor jBrighter() {
        return new JColor(this.brighter());
    }

    public float[] getFloatColorWAlpha() {
        return new float[]{getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f};
    }

    public float[] getFloatColor() {
        return new float[]{getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f};
    }
}
