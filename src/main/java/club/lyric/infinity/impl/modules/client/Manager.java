package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * if you select retarded enums from Formatting like CODE - your problem I'm not making a wrapper class
 */
@SuppressWarnings("unchecked")
public class Manager extends ModuleBase {
    public EnumSetting<Formatting> bracket = createEnum(new EnumSetting<>("Bracket", Formatting.BLACK, "Colour of brackets in Infinity's name."));
    public EnumSetting<Formatting> nameColour = createEnum(new EnumSetting<>("NameColour", Formatting.DARK_GRAY, "Colour of Infinity's name."));
    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.CLIENT);
    }
}
