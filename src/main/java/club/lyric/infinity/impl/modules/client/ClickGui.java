package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.gui.InfinityGUI;
import club.lyric.infinity.api.util.client.render.colors.JColor;

import java.awt.*;

public class ClickGui extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(Color.DARK_GRAY), true);

    public BooleanSetting shadow = new BooleanSetting("Shadow", true,this);

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.Client);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null || mc.player == null) return;

        InfinityGUI.getInstance().toggle();
        mc.mouse.unlockCursor();
    }

    @Override
    public void onDisable()
    {
        if (mc.world == null || mc.player == null) return;

        InfinityGUI.getInstance().toggle();
        if (mc.currentScreen == null) mc.mouse.lockCursor();
    }
}
