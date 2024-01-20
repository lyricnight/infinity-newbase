package club.lyric.infinity.api.setting.settings;

import imgui.ImGui;

import java.util.function.Predicate;

/**
 * extension of booleanSetting.
 */
public class BooleanRainbowSetting extends BooleanSetting {

    public String id;

    public BooleanRainbowSetting(String name, String id , Boolean defaultValue, Predicate<Boolean> visibility, String description) {
        super(name, defaultValue, visibility, description);
        this.id = id;
    }

    public BooleanRainbowSetting(String name, String id, Boolean defaultValue, String description) {
        super(name, defaultValue, description);
        this.id = id;
    }

    @Override
    public void render()
    {
        ImGui.pushID(id);

        ImGui.text(getName());

        if (ImGui.checkbox("" , getValue()))
        {
            setValue(!getValue());
        }

        ImGui.popID();
    }
}
