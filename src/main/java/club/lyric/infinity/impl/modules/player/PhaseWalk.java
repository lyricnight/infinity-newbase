package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.mc.movement.EntityMovementEvent;
import club.lyric.infinity.impl.events.mc.movement.MotionEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @author lyric
 * better phase than old infinity
 */

public final class PhaseWalk extends ModuleBase {
    public ModeSetting position = new ModeSetting("Position",this, "Standard", "Standard", "Low", "Zero", "Negative");

    public NumberSetting speed = new NumberSetting("Speed", this, 2, 1, 15,1);

    public NumberSetting delay = new NumberSetting("Delay", this, 2, 0,20, 1);

    //credit lithium for the idea
    public BooleanSetting down = new BooleanSetting("Down", false, this);

    public NumberSetting downDelay = new NumberSetting("DownDelay", this, 2, 1,10, 1);

    public BooleanSetting collisions = new BooleanSetting("Collisions", true, this);

    private final StopWatch.Single watch = new StopWatch.Single();

    private final StopWatch.Single downWatch = new StopWatch.Single();

    public PhaseWalk()
    {
        super("PhaseWalk", "For phasing.", Category.Player);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onMotion(MotionEvent event)
    {
        if (down.value() && mc.options.sneakKey.isPressed() && PlayerUtils.isPhasing() && mc.player.verticalCollision && downWatch.hasBeen(downDelay.getIValue() * 100L))
        {
            //prevents falling out of the world
            if(mc.player.getY() <= 1) return;

            double mod = mc.player.getY() - 0.003D;

            mc.player.setPosition(mc.player.getX(), mod, mc.player.getZ());

            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mod, mc.player.getZ(), true));
            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), getPosition(position.getMode()), mc.player.getZ(), true));

            mc.player.sidewaysSpeed = 0.0F;
            mc.player.upwardSpeed = 0.0F;
            mc.player.forwardSpeed = 0.0F;

            event.setX(0.0);
            event.setY(0.0);
            event.setZ(0.0);

            downWatch.reset();
        }
    }


    @SuppressWarnings("unused")
    @EventHandler
    public void onMove(EntityMovementEvent event)
    {
        if(nullCheck()) return;
        if (mc.player.horizontalCollision && watch.hasBeen(delay.getIValue() * 100L) && PlayerUtils.isPhasing() && !mc.player.isHoldingOntoLadder())
        {
            final double[] movementArray = MovementUtil.directionSpeed(speed.getValue() / 100.0);
            double x = mc.player.getX() + movementArray[0];
            double z = mc.player.getZ() + movementArray[1];

            mc.player.setPosition(x, mc.player.getY(), z);

            send(new PlayerMoveC2SPacket.PositionAndOnGround(x, mc.player.getY(), z, true));
            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), getPosition(position.getMode()), mc.player.getZ(), true));

            mc.player.sidewaysSpeed = 0.0F;
            mc.player.upwardSpeed = 0.0F;
            mc.player.forwardSpeed = 0.0F;

            event.setX(0.0);
            event.setY(0.0);
            event.setZ(0.0);

            watch.reset();
        }
    }

    private double getPosition(String mode)
    {
        if (mode.equals("Standard")) return 1337.0D;
        if (mode.equals("Low")) return 777.0D;
        if (mode.equals("Zero")) return 0.0D;
        if (mode.equals("Negative")) return -666.0D;
        Infinity.LOGGER.error("No position value set.");
        throw new RuntimeException("No Position Found!");
    }

    @Override
    public String moduleInformation()
    {
        return position.getMode();
    }
}
