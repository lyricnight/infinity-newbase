package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.Null;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import org.lwjgl.glfw.GLFW;

/**
 * @author vasler
 */
public final class Zoom extends ModuleBase
{
    public BindSetting zoomBind = new BindSetting("ZoomBind", -1, this);
    public NumberSetting amount = new NumberSetting("Amount", this, 0.2f, 0.1f, 0.9f, 0.1f);
    private final Animation zoom = new Animation(Easing.EASE_OUT_QUAD, 150);
    int previousFov;
    boolean zoomed;

    public Zoom()
    {
        super("Zoom", "aa", Category.Visual);
    }

    @Override
    public void onEnable()
    {
        if (Null.is()) return;

        previousFov = mc.options.getFov().getValue();
    }

    @Override
    public void onDisable()
    {
        if (Null.is()) return;

        mc.options.getFov().setValue(previousFov);
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event)
    {
        if (event.getKey() == zoomBind.getCode())
        {
            zoomed = true;
        }
        else if (event.getAction() == GLFW.GLFW_RELEASE)
        {
            zoomed = false;
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack)
    {
        if (Null.is()) return;

        if (!(zoom.getProgress() == 1.0) && zoomed)
            zoom.run(mc.options.getFov().getValue() * amount.getFValue());
        else {
            zoomed = false;
            zoom.run(previousFov);
            return;
        }

        mc.options.getFov().setValue((int) zoom.getValue());
    }
}
