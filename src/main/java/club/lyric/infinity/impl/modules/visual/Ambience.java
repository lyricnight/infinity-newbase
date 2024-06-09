package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Ambience extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), false);

    public Ambience()
    {
        super("Ambience", "Changes the worlds color.", Category.Visual);
    }

    @Override
    public void onEnable()
    {
        if (nullCheck()) return;

        mc.worldRenderer.reload();

    }

    @Override
    public void onRender2D(DrawContext context)
    {
        // boze method
        Render2DUtils.drawRect(context.getMatrices(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), 40).getRGB());
    }

    @Override
    public void onDisable()
    {
        if (nullCheck()) return;

        mc.worldRenderer.reload();
    }
}
