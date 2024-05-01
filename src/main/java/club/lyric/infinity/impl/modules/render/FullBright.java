package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import net.minecraft.world.LightType;

import java.awt.*;

/**
 * @author vasler
 */
public class FullBright extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Ambience");
    public BooleanSetting darkness = new BooleanSetting("Darkness", true, this);

    public FullBright() {
        super("FullBright", "Makes the game brighter.", Category.Render);
    }

    @Override
    public void onEnable()
    {

        if (nullCheck()) return;
    }

    // pasted from meteori dont care
    public int getLuminance(LightType type) {
        if (!isOn() || !mode.is("Normal")) return 0;
        return 15;
    }
}
