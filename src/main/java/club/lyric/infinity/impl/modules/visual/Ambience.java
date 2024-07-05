package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

/**
 * @author vasler
 */
public class Ambience extends ModuleBase {
    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), false);

    public Ambience() {
        super("Ambience", "Changes the worlds color.", Category.Visual);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        mc.worldRenderer.reload();

    }

    @Override
    public void onUpdate() {
        mc.world.getDimensionEffects().adjustFogColor(Vec3d.unpackRgb(color.getColor().getRGB()), 0);

        int time;
        //mc.world.setTimeOfDay();
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        mc.worldRenderer.reload();
    }
}
