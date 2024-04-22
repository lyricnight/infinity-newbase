package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

//this getting deleted
@Deprecated
public class Colours extends PersistentModuleBase {

    //public NumberSetting hue = new NumberSetting("Hue", this, 100, 0, 360, 1.0f);
    //public NumberSetting saturation = new NumberSetting("Saturation", this, 100, 0, 100, 1.0f);
    //public NumberSetting brightness = new NumberSetting("Brightness", this, 50, 0, 360, 1.0f);
    public ModeSetting colorMode = new ModeSetting("Mode", this, "Single", "Single", "Gradient");

    public NumberSetting gradientLength = new NumberSetting("Length", this, 30, 10, 130, 1);
    public NumberSetting gradientSpeed = new NumberSetting("Speed", this, 30, 1, 130, 1);


    public ColorSetting secondColor = new ColorSetting("SecondColor", this, new JColor(new Color(230, 230, 230)), true);
    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), false);

    public Colours() {
        super("Colours", "Colours", Category.Client);
    }

    public Color getColor()
    {
        //return ColorUtils.hslToColor(hue.getFValue(), saturation.getFValue(), brightness.getFValue(), 1.0f);
        return color.getColor();
    }

    public Color getGradientColor(int y) {
        double roundY = Math.sin(Math.toRadians((long) y * (gradientLength.getValue()) + System.currentTimeMillis() / gradientSpeed.getValue()));
        roundY = Math.abs(roundY);
        return ColorUtils.interpolate((float) MathHelper.clamp(roundY, 0.0, 1.0), color.getColor(), secondColor.getColor());
    }
}
