package club.lyric.infinity.api.util.client.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static float roundFloat(double number, int scale) {
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(scale, RoundingMode.FLOOR);
        return bd.floatValue();
    }
}
