package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.gui.Gui;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public final class GuiRewrite extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(64, 64, 124)), false);
    public NumberSetting height = new NumberSetting("Height", this, 16f, 10f, 20f, 1f);
    public NumberSetting speed = new NumberSetting("Speed", this, 250, 0, 1000, 1, "ms");

    public GuiRewrite()
    {
        super("ClickGui2", "Displays a graphical user interface.", Category.Client);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null) {
            disable();
            return;
        }

        if (mc.currentScreen == null)
        {
            mc.setScreen(new Gui());
        }
    }

}
