package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.clickgui.GUI;

public class ClickGui extends ModuleBase {

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.CLIENT);
    }

    @Override
    public void onEnable()
    {
        mc.setScreen(new GUI());
    }
}
