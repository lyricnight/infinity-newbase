package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for Setting<Integer>
 */

public class IntegerSetting extends Setting<Integer> {
    public IntegerSetting(String name, Integer defaultValue, Integer min, Integer max, String description) {
        super(name, defaultValue, min, max, description);
    }

    public IntegerSetting(String name, Integer defaultValue, Integer min, Integer max, Predicate<Integer> visibility, String description) {
        super(name, defaultValue, min, max, visibility, description);
    }

    @Override
    public Integer getValue()
    {
        return (int)this.value;
    }

    @Override
    public Integer getMax()
    {
        return (int)max;
    }

    @Override
    public Integer getMin()
    {
        return (int)min;
    }
}
