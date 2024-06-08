package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.client.gui.Menu;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author lyric
 */
public final class ClickGui extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(64, 64, 124)), false);

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.Client);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null) {
            disable();
            return;
        }
        Menu.getInstance().toggle();
        mc.mouse.unlockCursor();
    }

    @Override
    public void onDisable() {
        if (mc.world == null) {
            return;
        }
        mc.setScreenAndRender(null);
        Menu.getInstance().toggle();
        if (mc.currentScreen == null) mc.mouse.lockCursor();
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == GLFW.GLFW_KEY_ESCAPE)
        {
            setEnabled(false);
        }
    }
}
