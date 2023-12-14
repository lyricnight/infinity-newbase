package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for Setting<Enum>
 * may cause issues
 */

public class EnumSetting extends Setting<Enum> {
    public EnumSetting(String name, Enum defaultValue, String description) {
        super(name, defaultValue, description);
    }

    public EnumSetting(String name, Enum defaultValue, Predicate<Enum> visibility, String description) {
        super(name, defaultValue, visibility, description);
    }
}
