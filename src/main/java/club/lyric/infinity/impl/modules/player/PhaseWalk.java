package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.EntityMovementEvent;
import club.lyric.infinity.api.event.mc.MotionEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.enums.PhaseWalkEnum;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;


/**
 * @author lyric
 * better phase than old infinity
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class PhaseWalk extends ModuleBase {

    public EnumSetting<PhaseWalkEnum> position = createEnum(new EnumSetting<>("Position", PhaseWalkEnum.Standard, "Where to position ourselves on the y-axis"));


    public NumberSetting<Integer> speed = createNumber(new NumberSetting<>("Speed", 2, 1, 15, "How fast we move in phase."));

    public NumberSetting<Integer> delay = createNumber(new NumberSetting<>("Delay", 2, 0, 10, "Delay between going into a block"));

    //credit lithium for the idea
    public BooleanSetting down = createBool(new BooleanSetting("Down", false, "Whether to go down when pressing shift or not."));

    public NumberSetting<Integer> downDelay = createNumber(new NumberSetting<>("DownDelay", 2, 1, 10, v -> down.getValue(),"Delay in between going down blocks."));

    private final StopWatch.Single watch = new StopWatch.Single();

    private final StopWatch.Single downWatch = new StopWatch.Single();

    public PhaseWalk()
    {
        super("PhaseWalk", "For phasing.", Category.PLAYER);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onMotion(MotionEvent event)
    {
        if (down.getValue() && mc.options.sneakKey.isPressed() && PlayerUtils.isPhasing() && mc.player.verticalCollision && downWatch.hasBeen(downDelay.getValue() * 100L))
        {
            //prevents falling out of the world
            if(mc.player.getY() <= 1) return;

            double mod = mc.player.getY() - 0.003D;

            mc.player.setPosition(mc.player.getX(), mod, mc.player.getZ());

            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mod, mc.player.getZ(), true));
            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), position.getValue().getPosition(), mc.player.getZ(), true));

            //through a line in mc's code, i figured out that sidewaysSpeed is x and forwardSpeed is z, but there is also horizontalSpeed??
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
        if (mc.player.horizontalCollision && watch.hasBeen(delay.getValue() * 100L) && PlayerUtils.isPhasing() && !mc.player.isHoldingOntoLadder())
        {
            final double[] movementArray =
        }
    }

}
