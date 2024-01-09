package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.MotionEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.enums.PhaseWalkEnum;
import club.lyric.infinity.api.util.client.math.StopWatch;
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

    private final StopWatch.Single downWatch = new StopWatch.Single();

    public PhaseWalk()
    {
        super("PhaseWalk", "For phasing.", Category.PLAYER);
    }

    @EventHandler
    public void onMotion(MotionEvent event)
    {
        if (down.getValue() && mc.options.sneakKey.isPressed() && mc.player.verticalCollision && downWatch.hasBeen(downDelay.getValue() * 100L))
        {
            if(mc.player.getY() <= 1) return;

            double offset = mc.player.getY() - 0.003D;

            mc.player.setPosition(mc.player.getX(), offset, mc.player.getZ());

            send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), offset, mc.player.getZ(), true));
            

        }
    }




}
