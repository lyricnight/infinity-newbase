package club.lyric.infinity.api.module;

import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public class BlockModuleBase extends ModuleBase
{
    public NumberSetting bpt = new NumberSetting("Blocks Per Tick",this, 5,0,20, 1);
    public BooleanSetting swing = new BooleanSetting("Swing", true, this);



    public BlockModuleBase(String name, String description, Category category)
    {
        super(name, description, category);
    }


}