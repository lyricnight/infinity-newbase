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
public class ModeSetting extends Setting implements Renderable {
    public int index;

    public String[] modes;

    public ModeSetting(String name, ModuleBase moduleBase, String defaultMode, String... modes) {
        this.name = name;
        this.moduleBase = moduleBase;
        this.modes = modes;
        this.index = Arrays.stream(this.modes).toList().indexOf(defaultMode);
        moduleBase.addSettings(this);
    }

    public String getMode() {
        return modes[index];
    }

    public void setMode(String mode) {
        index = Arrays.stream(modes).toList().indexOf(mode);
    }

    public boolean is(String mode) {
        return (index == Arrays.stream(modes).toList().indexOf(mode));
    }

    public void increment() {
        index = (index + 1) % modes.length;
    }

    public void decrement() {
        index = (index - 1 + modes.length) % modes.length;
    }

    @Override
    public void render() {
        ImGui.pushID(moduleBase.getName() + "/" + name);

        ImGui.text(name);

        ImInt currentItem = new ImInt(index);

        ImGui.pushItemWidth(210f);
        ImGui.combo("", currentItem, modes);
        ImGui.popItemWidth();

        index = currentItem.get();

        ImGui.popID();
    }
}
