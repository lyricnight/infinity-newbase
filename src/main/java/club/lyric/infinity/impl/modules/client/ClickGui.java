package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ClickGui extends ModuleBase {

    public ColorSetting color = createColor(new ColorSetting("Color", new JColor(Color.GRAY), true, "Main gui color."));

    public BooleanSetting shadow = createBool(new BooleanSetting("Shadow", true, "Draws text with a shadow."));

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.CLIENT);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null || mc.player == null) return;

        Infinity.CLICK_GUI.toggle();
        mc.mouse.unlockCursor();
    }

    @Override
    public void onDisable()
    {
        if (mc.world == null || mc.player == null) return;

        Infinity.CLICK_GUI.toggle();
        if (mc.currentScreen == null) mc.mouse.lockCursor();
    }
}
