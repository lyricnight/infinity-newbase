package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {
    public FloatSetting(String name, Float defaultValue, Float min, Float max, String description) {
        super(name, defaultValue, min, max, description);
    }

    public FloatSetting(String name, Float defaultValue, Float min, Float max, Predicate<Float> visibility, String description) {
        super(name, defaultValue, min, max, visibility, description);
    }

    @Override
    public Float getValue()
    {
        return (float)this.value;
    }
}
