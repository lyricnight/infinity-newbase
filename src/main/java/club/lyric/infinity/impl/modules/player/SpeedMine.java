package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.mine.MineBlockEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author vasler
 */
public class SpeedMine extends ModuleBase
{

    public NumberSetting hitRange = new NumberSetting("HitRange", this, 6.0f, 1.0f, 7.0f, 0.1f);
    public NumberSetting delay = new NumberSetting("Delay", this, 3.0f, 1.0f, 30.0f, 1.0f);
    StopWatch.Single delayWatch = new StopWatch.Single();
    Direction direction;
    BlockPos pos;

    public SpeedMine()
    {
        super("SpeedMine", "Mines faster than vanilla.", Category.Player);
    }

    @Override
    public void onDisable()
    {
        direction = null;
        pos = null;
        delayWatch.reset();
    }

    @Override
    public void onUpdate()
    {
        if (mc.player == null || mc.world == null || mc.player.getAbilities().creativeMode) return;

        Block blocks = mc.world.getBlockState(pos).getBlock();

        // do we even need the falling block check? it's not like your going to hit a block midair lol
        if (blocks instanceof AirBlock || blocks instanceof FluidBlock || blocks instanceof FallingBlock) return;

        if (BlockUtils.getDistanceSq(pos) > MathUtils.square(hitRange.getValue()))
        {
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
            return;
        }

        if (pos != null)
        {
            if (delayWatch.hasBeen(delay.getLValue() * 100L))
            {
                send(new HandSwingC2SPacket(Hand.MAIN_HAND));
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
                delayWatch.reset();
            }
        }
    }

    @EventHandler
    public void onMineBlock(MineBlockEvent event)
    {
        if (mc.player == null || mc.world == null || mc.player.getAbilities().creativeMode) return;

        if (pos != null) {
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
        }

        Block blocks = mc.world.getBlockState(pos).getBlock();

        // do we even need the falling block check? it's not like your going to hit a block midair lol
        if (blocks instanceof AirBlock || blocks instanceof FluidBlock || blocks instanceof FallingBlock) return;

        direction = event.getDir();

        if (pos != null && direction != null)
        {
            event.setCancelled(true);

            send(new HandSwingC2SPacket(Hand.MAIN_HAND));
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, direction));

        }
    }
}
