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
}
