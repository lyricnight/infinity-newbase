package club.lyric.infinity.api.util.client.render.colors;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author vasler
 * but the author is actually lyric
 * but i made the class so fuck you
 */
public class ColorUtils implements IMinecraft {
    public static Color toColor(float red, float green, float blue, float alpha)
    {
        if (!(green < 0.0f) && !(green > 100.0f))
        {
            if (!(blue < 0.0f) && !(blue > 100.0f))
            {
                if (!(alpha < 0.0f) && !(alpha > 1.0f))
                {
                    red = red % 360.0f / 360.0f;
                    green /= 100.0f;
                    blue /= 100.0f;

                    float blueOff;
                    if (blue < 0.0)
                    {
                        blueOff = blue * (1.0f + green);
                    }
                    else
                    {
                        blueOff = blue + green - green * blue;
                    }

                    green = 2.0f * blue - blueOff;
                    blue = Math.max(0.0f, getFactor(green, blueOff, red + 0.33333334f));
                    float max = Math.max(0.0f, getFactor(green, blueOff, red));
                    green = Math.max(0.0f, getFactor(green, blueOff, red - 0.33333334f));
                    blue = Math.min(blue, 1.0f);
                    max = Math.min(max, 1.0f);
                    green = Math.min(green, 1.0f);
                    return new Color(blue, max, green, alpha);
                }
                else
                {
                    throw new IllegalArgumentException(
                            "Color parameter outside of expected range - Alpha");
                }
            }
            else
            {
                throw new IllegalArgumentException(
                        "Color parameter outside of expected range - Lightness");
            }
        }
        else
        {
            throw new IllegalArgumentException(
                    "Color parameter outside of expected range - Saturation");
        }
    }
    public static float getFactor(float red, float green, float blue)
    {
        if (blue < 0.0f)
        {
            ++blue;
        }

        if (blue > 1.0f)
        {
            --blue;
        }

        if (6.0f * blue < 1.0f)
        {
            return red + (green - red) * 6.0f * blue;
        }
        else if (2.0f * blue < 1.0f)
        {
            return green;
        }
        else
        {
            return 3.0F * blue < 2.0f
                    ? red + (green - red) * 6.0f * (0.6666667f - blue)
                    : red;
        }
    }

    /**
     * setting alpha
     * @param color - color in
     * @param alpha - alpha to set to
     * @return new color with that alpha
     */
    public static Color newAlpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    //why are these here?
    public static Color interpolate(float value, Color start, Color end) {
        final float sr = start.getRed() / 255.0f;
        final float sg = start.getGreen() / 255.0f;
        final float sb = start.getBlue() / 255.0f;
        final float sa = start.getAlpha() / 255.0f;
        final float er = end.getRed() / 255.0f;
        final float eg = end.getGreen() / 255.0f;
        final float eb = end.getBlue() / 255.0f;
        final float ea = end.getAlpha() / 255.0f;
        final float r = sr * value + er * (1.0f - value);
        final float g = sg * value + eg * (1.0f - value);
        final float b = sb * value + eb * (1.0f - value);
        final float a = sa * value + ea * (1.0f - value);
        return new Color(r, g, b, a);
    }

    public static Color darker(Color color, float factor) {
        return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color.getGreen() * factor), 0), Math.max((int) (color.getBlue() * factor), 0), color.getAlpha());
    }


}
