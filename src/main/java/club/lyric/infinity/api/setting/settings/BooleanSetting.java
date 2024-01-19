package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.RenderableSetting;
import club.lyric.infinity.api.setting.Setting;
import imgui.ImGui;

import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper setting representing Setting<Boolean>
 */

public class BooleanSetting extends Setting<Boolean> implements RenderableSetting {
    public BooleanSetting(String name, Boolean defaultValue, Predicate<Boolean> visibility, String description) {
        super(name, defaultValue, visibility, description);
    }

    public BooleanSetting(String name, Boolean defaultValue, String description)
    {
        super(name, defaultValue, description);
    }

    @Override
    public Boolean getValue()
    {
        return (Boolean)this.value;
    }

    @Override
    public void render()
    {
        ImGui.pushID(getModule().getName() + "/" + getName());

        ImGui.text(getName());
        if (ImGui.checkbox("", this.getValue())) {
            setValue(!getValue());
        }

        ImGui.popID();
    }
}
