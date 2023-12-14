package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for Setting<Double>
 */

public class DoubleSetting extends Setting<Double> {
    public DoubleSetting(String name, Double defaultValue, Double min, Double max, String description) {
        super(name, defaultValue, min, max, description);
    }

    public DoubleSetting(String name, Double defaultValue, Double min, Double max, Predicate<Double> visibility, String description) {
        super(name, defaultValue, min, max, visibility, description);
    }

    @Override
    public Double getValue()
    {
        return (double)this.value;
    }
}
