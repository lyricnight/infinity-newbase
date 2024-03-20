package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;

import java.awt.*;

public class Colours extends ModuleBase {

    //public NumberSetting hue = new NumberSetting("Hue", this, 100, 0, 360, 1.0f);
    //public NumberSetting saturation = new NumberSetting("Saturation", this, 100, 0, 100, 1.0f);
    //public NumberSetting brightness = new NumberSetting("Brightness", this, 50, 0, 360, 1.0f);

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), false);

    public Colours() {
        super("Colours", "Colours", Category.Client);
    }

    public Color getColor()
    {
        //return ColorUtils.hslToColor(hue.getFValue(), saturation.getFValue(), brightness.getFValue(), 1.0f);
        return color.getColor();
    }
}
