package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.mc.movement.PlayerMovementEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.manager.Managers;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec2f;

/**
 * @author vasler, zenov
 * The strafe is not sixset, but zenov helped me with it.
 */
@SuppressWarnings({"unused"})
public class Speed extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Strafe", "Strafe", "Jump");
    public BooleanSetting timer = new BooleanSetting("Timer", true, this);
    public BooleanSetting inLiquids = new BooleanSetting("InLiquids", true, this);
    double speed;
    int stage;
    double distance;
    float jumpHeight;
    // just chane the jump height every strafe mode
    boolean boost;

    public Speed() {
        super("Speed", "Increases your speed.", Category.Movement);
    }

    @Override
    public void onEnable() {
        stage = 1;

        if (mc.player != null)
        {

            speed = MovementUtil.getSpeed();
            distance = MovementUtil.getDistanceXZ();

        }

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
        stage = 1;
        speed = 0.0f;
        distance = 0.0;
        boost = false;
    }

    @Override
    public String moduleInformation() {
        if (nullCheck()) return "";
        return "Strafe";
    }

    @Override
    public void onUpdate()
    {
        if (mode.is("Jump"))
        {
            if (!mc.player.isOnGround() || !MovementUtil.movement()) return;

            mc.player.jump();
        }

    }

    @EventHandler
    public void onGang(PlayerMovementEvent event)
    {

        if (nullCheck() || mc.player.isSpectator() || !mc.player.isOnGround() || !MovementUtil.movement()) return;

        if (inLiquids.value() && (mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava())) return;

        event.setCancelled(true);


        float defaultSpeed = 0.2873f;

        if (mode.is("Strafe"))
        {

            if (stage == 1 && !(HoleUtils.isInHole(mc.player)))
            {

                speed = 1.35f * MovementUtil.calcEffects(defaultSpeed) - 0.01f;

            }
            if (stage == 2)
            {

                float yMotion = (float) (0.3999999463558197f + MovementUtil.getJumpSpeed());

                mc.player.setVelocity(mc.player.getVelocity().x, yMotion, mc.player.getVelocity().z);
                event.setY(yMotion);

                speed *= 1.395;

            }
            else if (stage == 3)
            {

                speed = distance - (0.66f * (distance - MovementUtil.calcEffects(defaultSpeed)));

                boost = !boost;
            }
            else
            {

                if ((!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0, mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) && stage > 0)
                {
                    stage = MovementUtil.movement() ? 1 : 0;
                }

                speed = distance - distance / 159.0f;

            }

            speed = Math.min(speed, MovementUtil.calcEffects(10.0));
            speed = Math.max(speed, MovementUtil.calcEffects(defaultSpeed));

            final Vec2f motion = MovementUtil.strafeSpeed((float) speed);

            event.setX(motion.x);
            event.setZ(motion.y);

            stage++;
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event)
    {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket)
        {
            stage = 1;
            speed = 0.0f;
            distance = 0.0;
            boost = false;
        }
    }
}
