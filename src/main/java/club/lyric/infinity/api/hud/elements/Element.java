package club.lyric.infinity.api.hud.elements;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public abstract class Element extends ModuleBase
{

    public NumberSetting x = new NumberSetting("hudX", this, 100f, 0f, mc.getWindow().getScaledWidth(), 1f);
    public NumberSetting y = new NumberSetting("hudY", this, 100f, 0f, mc.getWindow().getScaledHeight(), 1f);

    protected long width;
    protected long height;

    public Element(String name)
    {
        super(name, "", Category.Hud);
    }

    public abstract long setWidth();

    public abstract long setHeight();

}
