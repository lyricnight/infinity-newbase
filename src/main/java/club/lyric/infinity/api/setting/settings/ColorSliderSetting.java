package club.lyric.infinity.api.setting.settings;

import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImInt;

/**
 * for color sliders.
 */
public class ColorSliderSetting extends NumberSetting<Integer> {
    private String id;

    public ColorSliderSetting(String name, String id, int value, String description) {
        super(name, value, 0, 255, description);
        this.id = id;
    }

    @Override
    public void render()
    {
        ImGui.pushID(id);

        ImGui.text(getName());

        ImInt val = new ImInt(value);
        ImGui.pushItemWidth(160f);

        boolean changed = ImGui.sliderScalar("", ImGuiDataType.S32, val, min, max);
        ImGui.popItemWidth();

        //might be val.doubleValue()
        if (changed) this.value = val.intValue();

        ImGui.popID();
    }
}
