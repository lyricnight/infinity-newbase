package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for enumSettings
 * IDK if this works without having a GUI
 * @param <T> type of enum
 */
@SuppressWarnings({"rawtypes"})
public class EnumSetting<T extends Enum> extends Setting<T> {
    public EnumSetting(String name, T defaultValue, String description) {
        super(name, defaultValue, description);
    }

    public EnumSetting(String name, T defaultValue, Predicate<T> visibility, String description) {
        super(name, defaultValue, visibility, description);
    }

    @Override
    public void setValue(T value)
    {
        super.setValue(value);
    }

    @Override
    public T getValue()
    {
        return (T) this.value;
    }
}
