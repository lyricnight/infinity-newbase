package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.gui.Gui;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author vasler
 */
public final class ClickGUI extends ModuleBase {
    public NumberSetting frameHeight = new NumberSetting("FrameHeight", this, 16f, 10f, 20f, 1f);
    public NumberSetting buttonHeight = new NumberSetting("ButtonHeight", this, 13f, 10f, 20f, 1f);
    public NumberSetting frameWidth = new NumberSetting("FrameWidth", this, 6f, 1f, 10f, 1f);
    public ModeSetting position = new ModeSetting("Position", this, "Left", "Left", "Middle", "Right");
    public NumberSetting padding = new NumberSetting("Padding", this, 3.0f, 0.0f, 6.0f, 0.1f);
    public NumberSetting speed = new NumberSetting("Speed", this, 250, 0, 1000, 1, "ms");
    public BooleanSetting line = new BooleanSetting("Line", true, this);
    public BooleanSetting gear = new BooleanSetting("Gear", true, this);
    public BooleanSetting hover = new BooleanSetting("Hover", true, this);
    public BooleanSetting tint = new BooleanSetting("Tint", true, this);
    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(180, 50, 255)));
    public ColorSetting textColor = new ColorSetting("TextColor", this, new JColor(new Color(180, 50, 255)));
    public ColorSetting tintColor = new ColorSetting("TintColor", this, new JColor(new Color(180, 50, 255)));

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
