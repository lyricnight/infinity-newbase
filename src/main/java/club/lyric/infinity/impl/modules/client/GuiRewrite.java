package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.gui.GuiScreen;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import org.lwjgl.glfw.GLFW;

public final class GuiRewrite extends ModuleBase
{

    public GuiRewrite()
    {
        super("GuiRewrite", "Displays a graphical user interface.", Category.Client);
        setBind(GLFW.GLFW_KEY_P);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null) return;

        mc.setScreen(new GuiScreen());
    }

    @Override
    public void onDisable()
    {
        mc.setScreen(null);
    }
}
