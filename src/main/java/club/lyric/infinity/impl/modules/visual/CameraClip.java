package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;

/**
 * @author lyric
 */
public final class CameraClip extends ModuleBase {

    public NumberSetting range = new NumberSetting("Range", this, 5.0f, 1.0f, 10.0f, 0.1f);

    public CameraClip()
    {
        super("CameraClip", "Allows for your camera to clip into solid blocks", Category.Visual);
    }
}
