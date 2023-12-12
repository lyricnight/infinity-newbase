package club.lyric.infinity.api.setting;

/**
 * @author lyric
 * @param <T> capture of ? for our setting.
 */

public class Base<T> {

    /**
     * value of the setting which is a capture of T.
     */
    public T value;

    /**
     * aliases.
     */
    private final String[] names;

    public Base(T t, String ... stringArray) {
        this.value = t;
        this.names = stringArray;
    }

    public static String returnModifiedStringArray(String string) {
        int numOfChars = string.length();
        char[] cArray = new char[numOfChars];
        numOfChars--;
        int loop = numOfChars;
        int n3 = 2 << 3 ^ 1;
        int n5 = (2 ^ 5) << 4 ^ 3 << 1;
        while (loop >= 0) {
            int n6 = numOfChars--;
            cArray[n6] = (char)(string.charAt(n6) ^ n5);
            if (numOfChars < 0) break;
            int n7 = numOfChars--;
            cArray[n7] = (char)(string.charAt(n7) ^ n3);
            loop = numOfChars;
        }
        return new String(cArray);
    }
    public void setValue(T t) {
        this.value = t;
    }

    public T getValue() {
        return this.value;
    }

    public String[] getNames() {
        return this.names;
    }
}
