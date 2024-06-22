package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.keyboard.KeyUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import imgui.ImGui;
import org.lwjgl.glfw.GLFW;

/**
 * @author lyric
 * wrapper class for binds.
 */

public class BindSetting extends Setting implements Renderable {

    public int code;

    private boolean wasPressed;

    public BindSetting(String name, int code, ModuleBase moduleBase) {
        this.name = name;
        this.code = code;
        this.moduleBase = moduleBase;

        moduleBase.addSettings(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public void render() {
        ImGui.pushID(moduleBase.getName() + "/" + name);

        ImGui.text(name);

        if (!wasPressed) {
            wasPressed = ImGui.button(KeyUtils.getKeyName(getCode()));
        } else {
            ImGui.button("Press key...");
            EventBus.getInstance().register(this);
        }

        ImGui.popID();
    }

    @EventHandler(priority = 1)
    public void onKeyPress(KeyPressEvent event) {
        if (event.getAction() != GLFW.GLFW_RELEASE) {
            wasPressed = false;
            EventBus.getInstance().unregister(this);

            if (event.getKey() == GLFW.GLFW_KEY_ESCAPE) return;

            setCode(event.getKey() == GLFW.GLFW_KEY_DELETE ? 0 : event.getKey());
        }
    }
}
