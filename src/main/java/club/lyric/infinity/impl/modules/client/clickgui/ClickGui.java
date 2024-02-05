package club.lyric.infinity.impl.modules.client.clickgui;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.client.KeyPressEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ClickGui extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(Color.DARK_GRAY), false);

    public BooleanSetting shadow = new BooleanSetting("Shadow", true,this);

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.Client);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable()
    {
        mc.setScreenAndRender(GuiImGui.getClickGui());
    }

    @Override
    public void onDisable() {
        mc.setScreenAndRender(null);
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == GLFW.GLFW_KEY_ESCAPE)
        {
            setEnabled(false);
        }
    }
}
