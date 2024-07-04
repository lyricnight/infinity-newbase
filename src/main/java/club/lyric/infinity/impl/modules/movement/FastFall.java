package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;

public class FastFall extends ModuleBase {

    public NumberSetting height =
            new NumberSetting(
                    "Height",
                    this,
                    1.0f,
                    0.0f,
                    10.0f,
                    1.0f,
                    "b"
            );

    public NumberSetting speed =
            new NumberSetting(
                    "Speed",
                    this,
                    1.0f,
                    0.0f,
                    10.0f,
                    1.0f,
                    "bp/s"
            );
    public FastFall()
    {
        super("FastFall", "aa", Category.Movement);
    }

    @Override
    public void onUpdate()
    {

        if (nullCheck()) return;

        if (mc.options.jumpKey.isPressed()) return;

        if (mc.player.isOnGround())
        {
            mc.player.setVelocity(mc.player.getVelocity().x, -speed.getValue(), mc.player.getVelocity().z);
        }
    }
}
