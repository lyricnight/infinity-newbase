package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.gui.GuiScreen;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

/**
 * @author valser
 * i didnt make this - lyric
 */
@Deprecated
public final class GuiRewrite extends ModuleBase
{

    public NumberSetting scale = new NumberSetting("Scale", this, 1.0f, 0.3f, 2.0f, 0.1f);

    public GuiRewrite()
    {
        super("GuiRewrite", "Displays a graphical user interface.", Category.Client);
        setBind(GLFW.GLFW_KEY_P);
    }

    @Override
    public void onEnable()
    {
        if (mc.world == null) return;

        if (mc.currentScreen == null) {
            mc.setScreen(new GuiScreen());
        }
    }

    @Override
    public void onDisable()
    {
        mc.setScreen(null);
    }

    public float getScaledNumber() {
        return scale.getFValue();
    }
}
