package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.movement.EntityMovementEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.manager.Managers;

/**
 * @author vasler, zenov
 * The strafe is not sixset, but zenov helped me with it.
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class Speed extends ModuleBase {

    public BooleanSetting timer = new BooleanSetting("Timer", true, this);

    public BooleanSetting inLiquids = new BooleanSetting("InLiquids", true, this);

    double speed;

    public Speed() {
        super("Speed", "Increases your speed.", Category.Movement);
    }

    @Override
    public void onEnable()
    {
        if (timer.value())
        {
            Managers.TIMER.set(1.0888f);
        } else {
            Managers.TIMER.reset();
        }
    }

    @Override
    public void onDisable()
    {
        Managers.TIMER.reset();
    }

    @EventHandler
    public void onMovement(EntityMovementEvent event)
    {
        float defaultSpeed = 0.2873f;
        speed = (1.35 * MovementUtil.calcEffects(defaultSpeed) - 0.01);

        MovementUtil.createSpeed(speed);
    }

    @EventHandler
    public void onMove(EntityMovementEvent event) {


        if (nullCheck() || mc.player.isSpectator() || !mc.player.isOnGround() || !MovementUtil.movement()) return;

        if (inLiquids.value() && (mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava())) return;

        if (!(HoleUtils.isInHole(mc.player))) {

            double yMotion = 0.3999999463558197 + MovementUtil.getJumpSpeed();

            mc.player.setVelocity(mc.player.getVelocity().x, yMotion, mc.player.getVelocity().z);
            event.setY(yMotion);
        }
    }
}
