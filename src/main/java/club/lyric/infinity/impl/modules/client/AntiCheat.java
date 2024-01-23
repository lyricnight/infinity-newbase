package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.util.client.enums.RotationType;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * module that handles oll the global settings we may need
 */
public class AntiCheat extends ModuleBase {
    public BooleanSetting rotate = new BooleanSetting("Rotate", false, this);
    public EnumSetting<RotationType> rotationType = new EnumSetting<>("RotationType", this, RotationType.Packet);

    public BooleanSetting strictDirection = new BooleanSetting("StrictDirection", false,this);

    public AntiCheat()
    {
        super("AntiCheat", "Global Module for some specific settings.", Category.Client);
    }

    public static boolean getRotation()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).rotate.value();
    }

    public static RotationType getRotationType()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).rotationType.getMode();
    }

    public static boolean getStrictDirection()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).strictDirection.value();
    }
}
