package club.lyric.infinity.api.util.client;

import net.minecraft.util.Formatting;

/**
 * @author lyric
 * for some string methods we may need...
 */
public class StringUtils {
    public static String coloredString(String string, Formatting color) {
        String coloredString;

        coloredString = Formatting.RESET + "" + color + "" + string + "" + Formatting.RESET;

        return coloredString;
    }

    public static boolean contains(String name, String... items) {
        boolean flag = false;

        for (String i : items) {
            if (i.equalsIgnoreCase(name)) {
                flag = true;

                break;
            }
        }

        return flag;
    }
}
