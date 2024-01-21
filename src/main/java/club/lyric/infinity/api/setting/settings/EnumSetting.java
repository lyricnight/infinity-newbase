package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import imgui.ImGui;
import imgui.type.ImInt;

import java.util.Arrays;

/**
 * @author lyric
 */
@SuppressWarnings({"rawtypes"})
public class EnumSetting<T extends Enum<T>> extends Setting implements Renderable {
    int index;

    private final Enum[] modes;

    public EnumSetting(String name, ModuleBase moduleBase, Enum<T> defaultValue)
    {
        this.name = name;
        this.moduleBase = moduleBase;
        this.modes = defaultValue.getClass().getEnumConstants();
        this.index = Arrays.stream(this.modes).toList().indexOf(defaultValue);
    }

    public T getMode()
    {
        return (T) this.modes[index];
    }

    public void setMode(T mode)
    {
        this.index = Arrays.stream(this.modes).toList().indexOf(mode);
    }

    public boolean is(T mode) {
        return (this.index == Arrays.stream(this.modes).toList().indexOf(mode));
    }

    @Override
    public void render()
    {
        ImGui.pushID(moduleBase.getName() + "/" + getName());

        ImGui.text(name);

        ImInt currentItem = new ImInt(index);

        ImGui.pushItemWidth(170f);
        ImGui.combo("", currentItem, Arrays.toString(modes));
        ImGui.popItemWidth();

        this.index = currentItem.get();

        ImGui.popID();
    }
}
