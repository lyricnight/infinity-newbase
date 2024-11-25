package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.render.colors.JColor;

/**
 * color picker.
 */
public class ColorSetting extends Setting {
    private JColor color;

    public ColorSetting(String name, ModuleBase moduleBase, JColor color) {
        this.name = name;
        this.moduleBase = moduleBase;
        this.color = color;

        moduleBase.addSettings(this);
    }
    public JColor getValue() {
        return color;
    }

    public JColor getColor() {
        return color;
    }

    public void setColor(JColor color) {
        this.color = color;
    }
}
