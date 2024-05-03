package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * module that handles all anti-cheat related settings.
 * has static getters for all the anti-cheat settings.
 */
public final class AntiCheat extends PersistentModuleBase {
    public BooleanSetting rotate = new BooleanSetting("Rotate", false, this);
    public BooleanSetting strictDirection = new BooleanSetting("StrictDirection", false,this);
    public NumberSetting bpt = new NumberSetting("BPT", this, 4, 1, 10, 1);
    public BooleanSetting swing = new BooleanSetting("Swing", false, this);
    public BooleanSetting movementFix = new BooleanSetting("MovementFix", false, this);

    public AntiCheat()
    {
        super("AntiCheat", "Global Module for some specific settings.", Category.Client);
    }

    public static boolean getRotation()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).rotate.value();
    }

    public static boolean getStrictDirection()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).strictDirection.value();
    }

    public static int getBlocksPerTick()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).bpt.getIValue();
    }

    public static boolean getSwing()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).swing.value();
    }

    public static boolean getFix()
    {
        return Managers.MODULES.getModuleFromClass(AntiCheat.class).movementFix.value();
    }
}
