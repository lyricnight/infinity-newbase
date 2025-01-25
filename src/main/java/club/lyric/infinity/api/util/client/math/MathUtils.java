package club.lyric.infinity.api.util.client.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author lyric, valser
 * TODO clean this up
 */
public class MathUtils {

    public static float round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
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

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float square(float in) {
        return in * in;
    }

    public static double square(double in) {
        return in * in;
    }

    public static int angleDirection(float yaw, int disfunctional) {
        int angle = (int) (yaw + (double) 360 / (2 * disfunctional) + 0.5) % 360;

        if (angle < 0) {
            angle += 360;
        }

        return angle / (360 / disfunctional);
    }

    public static double normalize(double value, double max) {
        return (value - 0.0) / (max - 0.0);
    }
}
