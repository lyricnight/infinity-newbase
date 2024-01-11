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
    public BooleanSetting rotate = createBool(
            new BooleanSetting(
            "Rotate",
            false,
            "Rotations..."
    ));

    @SuppressWarnings("unchecked")
    public EnumSetting<RotationType> rotationType = createEnum(
            new EnumSetting<>(
            "RotationType",
            RotationType.Packet,
            v -> rotate.getValue(),
            "What type of rotation to use."
    ));

    public BooleanSetting strictDirection = createBool(
            new BooleanSetting(
            "StrictDirection",
            false,
            "Whether to have strictDirection checks."
    ));

    public AntiCheat()
    {
        super("AntiCheat", "Global Module for some specific settings.", Category.CLIENT);
    }

    public static boolean getRotation()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).rotate.getValue();
    }

    public static RotationType getRotationType()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).rotationType.getValue();
    }

    public static boolean getStrictDirection()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).strictDirection.getValue();
    }
}
