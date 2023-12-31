package club.lyric.infinity.api.util.math;

import java.util.UUID;

/**
 * @author lyric
 * used to generate a unique int value for a given class.
 */
public class UniqueIdentifier {
    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
