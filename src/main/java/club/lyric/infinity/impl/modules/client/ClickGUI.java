package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.gui.Gui;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

/**
 * @author vasler
 */
public final class ClickGUI extends ModuleBase {
    public NumberSetting frameHeight = new NumberSetting("FrameHeight", this, 16f, 10f, 20f, 1f);
    public NumberSetting buttonHeight = new NumberSetting("ButtonHeight", this, 13f, 10f, 20f, 1f);
    public ModeSetting position = new ModeSetting("Position", this, "Left", "Left", "Middle", "Right");
    public NumberSetting padding = new NumberSetting("Padding", this, 3.0f, 0.0f, 6.0f, 0.1f);
    public NumberSetting speed = new NumberSetting("Speed", this, 250, 0, 1000, 1, "ms");
    public BooleanSetting line = new BooleanSetting("Line", true, this);
    public BooleanSetting gear = new BooleanSetting("Gear", true, this);

    public ClickGUI() {
        super("ClickGui", "Displays a graphical user interface.", Category.Client);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        if (mc.world == null) {
            disable();
            return;
        }

        if (mc.currentScreen == null) {
            mc.setScreen(new Gui());
        }
    }
}
