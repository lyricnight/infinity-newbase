package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for Setting<String>
 */

public class StringSetting extends Setting<String> {
    public StringSetting(String name, String defaultValue, String description) {
        super(name, defaultValue, description);
    }

    public StringSetting(String name, String defaultValue, Predicate<String> visibility, String description) {
        super(name, defaultValue, visibility, description);
    }

    @Override
    public String getValue()
    {
        return (String)this.value;
    }
}
