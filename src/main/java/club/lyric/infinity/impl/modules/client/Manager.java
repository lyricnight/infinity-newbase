package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;

/**
 * @author lyric
 */
public final class Manager extends ModuleBase {
    public ModeSetting bracket = new ModeSetting("Bracket", this, "Black", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public ModeSetting nameColour = new ModeSetting("NameColour", this, "DarkGray", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.Client);
    }
}
