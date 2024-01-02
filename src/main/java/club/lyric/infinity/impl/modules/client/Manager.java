package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * if you select retarded enums from Formatting like CODE - your problem I'm not making a wrapper class
 */
public class Manager extends ModuleBase {
    //these are actually used, see ChatUtils.
    public Setting<Formatting> bracket = create(new Setting<>("Bracket", Formatting.BLACK, "Colour of brackets in Infinity's name."));
    public Setting<Formatting> nameColour = create(new Setting<>("NameColour", Formatting.DARK_GRAY, "Colour of Infinity's name."));

    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.CLIENT);
    }
}
