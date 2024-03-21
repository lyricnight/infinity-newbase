package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;

public class FullBright extends ModuleBase {

    public BooleanSetting darkness = new BooleanSetting("Darkness", true, this);

    public FullBright() {
        super("FullBright", "Makes the game brighter.", Category.Render);
    }
}
