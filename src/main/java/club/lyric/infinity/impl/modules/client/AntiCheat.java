package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;

/**
 * @author lyric
 * module that handles oll the global settings we may need
 */
public class AntiCheat extends ModuleBase {

    public BooleanSetting rotate = new BooleanSetting(
            "Rotate",
            false,
            "Rotations..."
    );

    public EnumSetting rotationType = new EnumSetting(
            "RotationType",
            RotationType.Packet,
            v -> rotate.getValue(),
            "What type of rotation to use."
    );

    public BooleanSetting strictDirection = new BooleanSetting(
            "StrictDirection",
            false,
            "Whether to have strictDirection checks."
    );


    public AntiCheat()
    {
        super("AntiCheat", "Global Module for some specific settings.", Category.CLIENT);
        instantiate(this, rotate, strictDirection);
        instantiate(this, rotationType);
    }


    private enum RotationType
    {
        Normal,
        Packet
    }

}
