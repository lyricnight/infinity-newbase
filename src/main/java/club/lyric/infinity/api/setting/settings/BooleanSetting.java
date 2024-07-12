package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;

/**
 * @author lyric
 */
public class BooleanSetting extends Setting {

    private boolean value;

    public BooleanSetting(String name, boolean value, ModuleBase moduleBase) {
        this.name = name;
        this.value = value;
        this.moduleBase = moduleBase;

        if (moduleBase != null) moduleBase.addSettings(this);
    }

    /**
     * gets value
     *
     * @return - value of setting
     */
    public boolean value() {
        return value;
    }

    /**
     * sets value
     *
     * @param value - value to set to
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * switches the value of the setting.
     */
    public void toggle() {
        value = !value;
    }
}
