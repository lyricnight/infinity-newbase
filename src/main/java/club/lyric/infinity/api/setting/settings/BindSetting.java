package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Setting;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for binds.
 */

public class BindSetting extends Setting<Bind> {

    Bind bind;

    public BindSetting(String name, Bind defaultValue, String description) {
        super(name, defaultValue, description);

        bind = defaultValue;
    }

    public BindSetting(String name, Bind defaultValue, Predicate<Bind> visibility, String description) {
        super(name, defaultValue, visibility, description);

        bind = defaultValue;
    }

    public int getKey()
    {
        return this.bind.getKey();
    }

}
