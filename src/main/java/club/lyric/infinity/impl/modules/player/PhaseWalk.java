package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;

/**
 * @author lyric
 */

public final class PhaseWalk extends ModuleBase {
    public BooleanSetting sync = new BooleanSetting("Sync", false, this);
    public BooleanSetting resync = new BooleanSetting("ReSync", false, this);

    public BooleanSetting check = new BooleanSetting("Check", false, this);
    public BooleanSetting ongod = new BooleanSetting("OnGround", false, this);
    public BooleanSetting down = new BooleanSetting("Down", false, this);
    public BooleanSetting up = new BooleanSetting("Up", false, this);
    public BooleanSetting busyWait = new BooleanSetting("BusyWait", false, this);
    public BooleanSetting rotation = new BooleanSetting("RotationHold", false, this);

    public NumberSetting pre = new NumberSetting("Pre", this, 5, 0, 20, 1);
    public NumberSetting post = new NumberSetting("Post", this, 5, 0, 20, 1);



    public PhaseWalk()
    {
        super("PhaseWalk", "Allows walking through walls.", Category.Player);
    }

}
