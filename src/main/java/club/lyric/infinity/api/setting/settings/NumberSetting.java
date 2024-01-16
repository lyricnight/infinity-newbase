package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author bel
 * Use this instead of floatsettings, intsettings and doublesettings? (Much easier to work with.)
 * @param <T>
 */
public class NumberSetting<T extends Number> extends Setting<T> {
    private final boolean clamp;

    public NumberSetting(String name, T defaultValue, T min, T max, String description) {
        super(name, defaultValue, description);
        clamp = true;
        this.min = min;
        this.max = max;
    }

    public NumberSetting(String name, T defaultValue, T min, T max, Predicate<T> visibility , String description) {
        super(name, defaultValue, visibility, description);
        clamp = true;
        this.min = min;
        this.max = max;
    }
    
    @Override
    public void setValue(T value) {
        if (clamp & (max != null && min != null)) {
            if (value instanceof Integer) {
                if (value.intValue() > max.intValue()) {
                    value = max;
                } else if (value.intValue() < min.intValue()) {
                    value = min;
                }
            } else if (value instanceof Float) {
                if (value.floatValue() > max.floatValue()) {
                    value = max;
                } else if (value.floatValue() < min.floatValue()) {
                    value = min;
                }
            } else if (value instanceof Double) {
                if (value.doubleValue() > max.doubleValue()) {
                    value = max;
                } else if (value.doubleValue() < min.doubleValue()) {
                    value = min;
                }
            } else if (value instanceof Long) {
                if (value.longValue() > max.longValue()) {
                    value = max;
                } else if (value.longValue() < min.longValue()) {
                    value = min;
                }
            } else if (value instanceof Short) {
                if (value.shortValue() > max.shortValue()) {
                    value = max;
                } else if (value.shortValue() < min.shortValue()) {
                    value = min;
                }
            } else if (value instanceof Byte) {
                if (value.byteValue() > max.byteValue()) {
                    value = max;
                } else if (value.byteValue() < min.byteValue()) {
                    value = min;
                }
            }
        }
        super.setValue(value);
    }
}