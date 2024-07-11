package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.math.Null;
import net.minecraft.world.LightType;

/**
 * @author vasler
 */
public class FullBright extends ModuleBase {

    public BooleanSetting darkness = new BooleanSetting("Darkness", true, this);

    public FullBright() {
        super("FullBright", "Makes the game brighter.", Category.Visual);
    }

    @Override
    public void onEnable() {
        if (Null.is()) return;

        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        if (Null.is()) return;

        mc.worldRenderer.reload();
    }


    public int getLuminance(LightType type) {
        if (!isOn()) return 0;
        return 15;
    }
}
