package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.RenderableSetting;
import club.lyric.infinity.api.setting.Setting;
import imgui.ImGui;
import imgui.type.ImInt;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author lyric
 * wrapper class for enumSettings
 * @param <T> type of enum
 */
@SuppressWarnings({"rawtypes"})
public class EnumSetting<T extends Enum> extends Setting<T> implements RenderableSetting {

    int index;

    public EnumSetting(String name, T defaultValue, String description) {
        super(name, defaultValue, description);
        index = Arrays.stream(getValue().getClass().getEnumConstants()).toList().indexOf(defaultValue);
    }

    public EnumSetting(String name, T defaultValue, Predicate<T> visibility, String description) {
        super(name, defaultValue, visibility, description);
        index = Arrays.stream(getValue().getClass().getEnumConstants()).toList().indexOf(defaultValue);

    }

    @Override
    public void setValue(T value) {
        index = Arrays.stream(getValue().getClass().getEnumConstants()).toList().indexOf(value);
        super.setValue(value);
    }

    @Override
    public T getValue() {
        return (T) this.value;
    }

    @Override
    public void render()
    {
        ImGui.pushID(module.getName() + "/" + getName());

        ImGui.text(getName());

        ImInt current = new ImInt(Arrays.stream(getValue().getClass().getEnumConstants()).toList().indexOf(getDefaultValue()));

        ImGui.pushItemWidth(170f);
        ImGui.combo("", current, Arrays.toString(Arrays.stream(getValue().getClass().getEnumConstants()).toArray()));

        ImGui.popItemWidth();

        index = current.get();

        ImGui.popID();
    }
}
