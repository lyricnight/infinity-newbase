package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * if you select retarded enums from Formatting like CODE - your problem I'm not making a wrapper class
 */
public class Manager extends ModuleBase {
    public EnumSetting bracket = new EnumSetting("Bracket", this, Formatting.BLACK);
    public EnumSetting nameColour = new EnumSetting("NameColour", this, Formatting.DARK_GRAY);
    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.Client);
    }
}
