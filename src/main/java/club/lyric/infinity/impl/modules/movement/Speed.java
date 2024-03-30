package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.movement.EntityMovementEvent;
import club.lyric.infinity.api.event.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

/**
 * @author vasler, zenov
 * Sixset speed ported to 1.20.4 with more modes.
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class Speed extends ModuleBase {

    double distance;
    double lastDist;
    double speed;
    int stage;

    public Speed() {
        super("Speed", "Increases your speed.", Category.Movement);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            speed = MovementUtil.getSpeed();
            distance = MovementUtil.getDistanceXZ();
        }
        stage = 4;
        lastDist = 0.0;
    }

    @EventHandler
    public void onMove(EntityMovementEvent event)
    {
        if (nullCheck() || !MovementUtil.movement() || mc.player.isSpectator()) return;

        if (mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava() || mc.player.isHoldingOntoLadder() || !(mc.world.getCollisions(mc.player, mc.player.getBoundingBox()) == null)) return;

        if (stage == 1)
        {
            speed = 1.35 * MovementUtil.calcEffects(2873.0) - 0.01;
        }
        else if (stage == 2)
        {
            //if (!(mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava() || HoleUtils.isInHole(mc.player) && (!EntityUtil.isInHole(mc.player) || step.getValue()|| shouldBoost(true) && kbStep.getValue()))) {
            if (!(mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava() || HoleUtils.isInHole(mc.player))) {
                double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                PlayerUtils.setMotionY(yMotion);
                event.setY(yMotion);
            }
            //speed = shouldBoost(false) ? kbFactor.getValue() / 10.0 : speed * (boost ? 1.6835 : 1.395);
        }
        /**else if (stage == 3)
        {
            if (shouldBoost(true) && kbStep.getValue()) {
                MovementUtil.step(kbHeight.getValue());
                stepFlag = true;
            }
            speed = shouldBoost(false) ? kbFactor.getValue() / 10.0 : distance - 0.66 * (distance - MovementUtil.calcEffects(0.2873));
            boost = !boost;
        }*/
        else
        {
            if ((mc.world.isSpaceEmpty(null, mc.player.getBoundingBox().offset(0.0, mc.player.getVelocity().getY(), 0.0)) || mc.player.verticalCollision) && stage > 0) {
                stage = MovementUtil.movement() ? 1 : 0;
            }
            speed = distance - distance / 159.0;
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e)
    {
        if (!nullCheck() && e.getPacket() instanceof PlayerPositionLookS2CPacket)
        {
            distance = 0.0;
            speed = 0.0;
            stage = 4;
        }
    }

    @EventHandler
    public void onWalkingUpdate(UpdateWalkingPlayerEvent e) {
        distance = MovementUtil.getDistanceXZ();
    }

}
