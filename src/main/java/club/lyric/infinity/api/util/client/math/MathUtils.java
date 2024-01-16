package club.lyric.infinity.api.util.client.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author lyric, valser
 */
public class MathUtils {

    public static float round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }


    public static float roundFloat(double number, int scale) {
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(scale, RoundingMode.FLOOR);
        return bd.floatValue();
    }

    public static double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        return (d2 > d1 ? low : high);
    }

    /**
     * clamps
     */

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float calculateHue(float x, float y, float hueSpeedX, float hueSpeedY) {
        return (float) (Math.sin((x * hueSpeedX) + (y * hueSpeedY) + System.currentTimeMillis() * 0.001) + 1.0) / 2.0f;
    }

    /**
     * middle of some variables
     */
    public static float getMiddle(float a, float b, float c) {
        return (a + b / 2) - c / 2;
    }
}
