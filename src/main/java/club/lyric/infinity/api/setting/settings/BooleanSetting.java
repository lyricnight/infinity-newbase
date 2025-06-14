package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import imgui.ImGui;
import lombok.Setter;

/**
 * @author lyric
 */
@Setter
public class BooleanSetting extends Setting implements Renderable {

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
     * switches the value of the setting.
     */
    public void toggle() {
        value = !value;
    }

    @Override
    public void render() {
        ImGui.pushID(moduleBase.getName() + "/" + name);

        if (ImGui.checkbox("", value)) {
            setValue(!value);
        }
        ImGui.sameLine();
        ImGui.text(name);

        ImGui.popID();
    }
}
