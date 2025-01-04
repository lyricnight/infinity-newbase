package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;

/**
 * @author lyric
 */
public class CustomFont extends ModuleBase {

    public BooleanSetting shadow = new BooleanSetting("Shadow", true, this);

    public CustomFont()
    {
        super("CustomFont", "Toggle module.", Category.CLIENT);
    }
}
