package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Base;

/**
 * @author lyric
 * enums
 * @param <T> enum.
 */

public class EnumSetting<T extends Enum<?>> extends Base<T> {
    public EnumSetting(T t, String... stringArray) {
        super(t, stringArray);
    }

    public void increment() {
        T[] t = this.getEnums();
        int n = this.getValue().ordinal();
        if (++n > (t).length - 1) n = 0;
        this.setValue(t[n]);
    }

    public T[] getEnums() {
        return (T[]) ((Enum)this.getValue()).getClass().getEnumConstants();
    }

    public void deduct() {
        T[] t = this.getEnums();
        int n = this.getValue().ordinal();
        if (--n < 0) n = (t).length - 1;
        this.setValue(t[n]);
    }

    /**
     * this searches if the enum given is actually valid - so you can't pass invalid values to the setting, which can cause crashes.
     * USE THIS BEFORE ASSIGNING AN ENUMSETTING ANYTHING.
     * @param string enum in.
     */
    public void search(String string) {
        int n;
        T[] t = this.getEnums();
        int n2 = t.length;
        int n3 = n = 0;
        while (n3 < n2) {
            T t2 = t[n];
            if (((Enum)t2).name().equalsIgnoreCase(string)) {
                this.setValue(t2);
                return;
            }
            n3 = ++n;
        }
    }
    public String toString() {
        return ((Enum)this.getValue()).name().charAt(0) + ((Enum)this.getValue()).name().toLowerCase().replaceFirst(Character.toString(((Enum)this.getValue()).name().charAt(0)).toLowerCase(), "");
    }
}

