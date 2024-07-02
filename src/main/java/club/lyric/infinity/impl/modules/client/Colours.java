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

public class Colours extends PersistentModuleBase {

    public ModeSetting colorMode = new ModeSetting("Mode", this, "Single", "Single", "Gradient");
    public NumberSetting gradientSpeed = new NumberSetting("Speed", this, 1.0f, 0.1f, 3.0f, 0.1f);


    public ColorSetting color = new ColorSetting("Start", this, new JColor(new Color(104, 71, 141)), false);
    public ColorSetting secondColor = new ColorSetting("End", this, new JColor(new Color(230, 230, 230)), true);

    public ColorSetting friendColor = new ColorSetting("FriendColor", this, new JColor(new Color(180, 180, 255)), true);
    public ColorSetting sneakColor = new ColorSetting("SneakColor", this, new JColor(new Color(255, 180, 255)), true);

    public Colours() {
        super("Colours", "Colours", Category.Client);
    }

    public Color getColor() {
        return color.getColor();
    }

    public Color getGradientColor(int y) {
        double roundY = Math.sin(Math.toRadians((long) y * (130) + System.currentTimeMillis() / (gradientSpeed.getValue() * 10)));
        roundY = Math.abs(roundY);
        return ColorUtils.interpolate((float) MathHelper.clamp(roundY, 0.0, 1.0), color.getColor(), secondColor.getColor());
    }
}
