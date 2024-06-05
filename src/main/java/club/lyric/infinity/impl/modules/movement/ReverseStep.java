package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;

@SuppressWarnings("ConstantConditions")
public class ReverseStep extends ModuleBase {

    public NumberSetting height = new NumberSetting("Height", this, 2f, 1f, 10f, 1f);
    public NumberSetting speed = new NumberSetting("Speed", this, 2f, 1f, 10f, 1f);
    StopWatch.Single stopWatch = new StopWatch.Single();

    public ReverseStep()
    {
        super("ReverseStep", "unsteps", Category.Movement);
    }

    @Override
    public void onUpdate()
    {
        if (mc.player.isInsideWaterOrBubbleColumn() && mc.player.getAir() > 0 ||
                mc.player.isTouchingWater() ||
                mc.player.getAbilities().flying ||
                mc.player.isFallFlying() ||
                mc.player.isClimbing() ||
                mc.options.jumpKey.isPressed() ||
                mc.options.sneakKey.isPressed())
            return;

        if (mc.player.isOnGround() && stopWatch.hasBeen(250))
            if (mc.player.fallDistance < height.getFValue())
                mc.player.setVelocity(mc.player.getVelocity().x, -speed.getFValue(), mc.player.getVelocity().z);

        stopWatch.reset();
    }
}
