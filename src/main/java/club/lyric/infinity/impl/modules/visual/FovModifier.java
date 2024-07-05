package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public class FovModifier extends ModuleBase
{
    public BooleanSetting staticFov = new BooleanSetting("Static", true, this);
    public NumberSetting aiming = new NumberSetting("Aiming", this, 1.0f, 0.0f, 2.0f, 0.1f, "x");
    public NumberSetting sprinting = new NumberSetting("Sprinting", this, 1.0f, 0.0f, 2.0f, 0.1f, "x");
    public NumberSetting speed = new NumberSetting("Speed", this, 1.0f, 0.0f, 2.0f, 0.1f, "x");
    public NumberSetting flying = new NumberSetting("Flying", this, 1.0f, 0.0f, 2.0f, 0.1f, "x");
    public FovModifier()
    {
        super("FovModifier", "aa", Category.Visual);
    }
}
