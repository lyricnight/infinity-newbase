package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * module that handles all anti-cheat related settings.
 */
public final class AntiCheat extends PersistentModuleBase {
    public final BooleanSetting rotate = new BooleanSetting("Rotate", false, this);
    public final NumberSetting holdingTime = new NumberSetting("HoldingTime", this, 0f, 5f, 20f, 0.5f);
    public final BooleanSetting strictDirection = new BooleanSetting("StrictDirection", false, this);
    public final NumberSetting bpt = new NumberSetting("BPT", this, 4, 1, 20, 1);
    public final BooleanSetting movementFix = new BooleanSetting("MovementFix", false, this);
    public final BooleanSetting lower = new BooleanSetting("1.12 Mode", false, this);

    public AntiCheat() {
        super("AntiCheat", "Global Module for some specific settings.", Category.CLIENT);
    }

    /**
     * sets values into AntiCheatManager.
     * TODO change this to a better system.
     */
    public void set()
    {
        Managers.ANTICHEAT.set(rotate.value(), strictDirection.value(), movementFix.value(), lower.value(), holdingTime.getFValue(), bpt.getIValue());
    }
}
