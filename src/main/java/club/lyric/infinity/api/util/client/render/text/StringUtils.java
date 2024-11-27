package club.lyric.infinity.api.util.client.render.text;

/**
 * @author lyric
 * for some string methods we may need...
 */
public class StringUtils {
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
