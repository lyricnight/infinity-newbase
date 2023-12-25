package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public class YawLock extends ModuleBase {

    public NumberSetting<Float> yaw = new NumberSetting<>(
            "Yaw",
            0f,
            -180f,
            180f,
            0.1f,
            "Locks your yaw to the value set."
    );

    public NumberSetting<Float> pitch = new NumberSetting<>(
            "Pitch",
            0f,
            -90f,
            90f,
            0.1f,
            "Locks your pitch to the value set."
    );

    public YawLock()
    {
        super("YawLock", "Locks your yaw and pitch.", Category.PLAYER);
        instantiate(this, yaw, pitch);
    }

    @Override
    public void onUpdate()
    {
        mc.player.setYaw(yaw.getValue());
        mc.player.setPitch(pitch.getValue());
    }
}
