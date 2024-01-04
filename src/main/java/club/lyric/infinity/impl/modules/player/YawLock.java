package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public class YawLock extends ModuleBase {

    public NumberSetting<Float> yaw = createNumber(
            new NumberSetting<>(
            "Yaw",
            0f,
            -180f,
            180f,
            0.1f,
            "Locks your yaw to the value set."
    ));

    public NumberSetting<Float> pitch = createNumber(
            new NumberSetting<>(
            "Pitch",
            0f,
            -90f,
            90f,
            0.1f,
            "Locks your pitch to the value set."
    ));

    public YawLock()
    {
        super("YawLock", "Locks your yaw and pitch.", Category.PLAYER);
    }

    @Override
    public void onUpdate()
    {
        if (nullCheck())
        {
            return;
        }
        mc.player.setYaw(yaw.getValue());
        mc.player.setPitch(pitch.getValue());
    }
}
