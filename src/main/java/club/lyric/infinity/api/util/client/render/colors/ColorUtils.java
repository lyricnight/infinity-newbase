package club.lyric.infinity.api.util.client.render.colors;

import club.lyric.infinity.api.util.client.math.apache.ApacheMath;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author vasler
 * but the author is actually lyric
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
                    blue = ApacheMath.max(0.0f, getFactor(green, blueOff, red + 0.33333334f));
                    float max = ApacheMath.max(0.0f, getFactor(green, blueOff, red));
                    green = ApacheMath.max(0.0f, getFactor(green, blueOff, red - 0.33333334f));
                    blue = ApacheMath.min(blue, 1.0f);
                    max = ApacheMath.min(max, 1.0f);
                    green = ApacheMath.min(green, 1.0f);
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

    public static Color darken(Color color, int amount)
    {
        int red = Math.max(color.getRed() - amount, 20);
        int green = Math.max(color.getGreen() - amount, 20);
        int blue = Math.max(color.getBlue() - amount, 20);

        return new Color(red, green, blue, color.getAlpha());
    }

    public static Color hslToColor(float hue, float sat, float light, float alpha)
    {
        if (sat < 0.0f || sat > 100.0f)
        {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
        if (light < 0.0f || light > 100.0f)
        {
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
        }
        if (alpha < 0.0f || alpha > 1.0f)
        {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        }
        hue %= 360.0f;
        float f5;
        f5 = (double)light < 0.5 ? light * (1.0f + sat) : (light /= 100.0f) + (sat /= 100.0f) - sat * light;
        sat = 2.0f * light - f5;
        light = Math.max(0.0f, colorCalc(sat, f5, (hue /= 360.0f) + 0.33333334f));
        float f6 = Math.max(0.0f, colorCalc(sat, f5, hue));
        sat = Math.max(0.0f, colorCalc(sat, f5, hue - 0.33333334f));
        light = Math.min(light, 1.0f);
        f6 = Math.min(f6, 1.0f);
        sat = Math.min(sat, 1.0f);
        return new Color(light, f6, sat, alpha);
    }

    private static float colorCalc(float f, float f2, float f3)
    {
        if (f3 < 0.0f)
        {
            f3 += 1.0f;
        }
        if (f3 > 1.0f)
        {
            f3 -= 1.0f;
        }
        if (6.0f * f3 < 1.0f)
        {
            float f4 = f;
            return f4 + (f2 - f4) * 6.0f * f3;
        }
        if (2.0f * f3 < 1.0f)
        {
            return f2;
        }
        if (3.0f * f3 < 2.0f)
        {
            float f5 = f;
            return f5 + (f2 - f5) * 6.0f * (0.6666667f - f3);
        }
        return f;
    }

    // INFINITY OLD DEF PASTED
    public static Color interpolate(final float value, final Color start, final Color end) {
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

}
