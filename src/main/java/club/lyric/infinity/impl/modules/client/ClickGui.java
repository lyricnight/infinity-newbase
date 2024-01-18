package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.impl.clickgui.GUI;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends ModuleBase {

    public BooleanSetting shadow = createBool(new BooleanSetting("Shadow", true, "Draws text with a shadow."));

    public ClickGui()
    {
        super("ClickGui", "Displays a graphical user interface.", Category.CLIENT);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable()
    {
        if (nullCheck())
        {
            return;
        }
        mc.setScreen(GUI.getClickGui());
    }
}
