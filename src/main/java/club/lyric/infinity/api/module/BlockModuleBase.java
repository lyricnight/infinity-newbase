package club.lyric.infinity.api.module;

import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class BlockModuleBase extends ModuleBase
{

    public NumberSetting range = new NumberSetting("Range", this, 6.0f, 1.0f, 7.0f, 0.1f);
    public BooleanSetting swing = new BooleanSetting("Swing", true, this);

    public BlockModuleBase(String name, String description, Category category)
    {
        super(name, description, category);
    }

    @Override
    public void onEnable()
    {

    }

    public void reset()
    {
        // Module implements the method
    }

    private void placeBlock(BlockHitResult result)
    {

        if (!(BlockUtils.getDistanceSq(result.getBlockPos()) >= MathUtils.square(range.getFValue()))) return;

        sendSeq(id -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, id));


        if (swing.value())
        {
            send(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }

        if (mc.world.getBlockState(result.getBlockPos().offset(result.getSide())).getBlock().equals(Blocks.AIR)) return;


    }

    private boolean isPlaceable(BlockPos pos) {

        Block block = mc.world.getBlockState(pos).getBlock();

        if (block == Blocks.AIR || block == Blocks.SNOW || block == Blocks.TALL_GRASS || block == Blocks.SHORT_GRASS || block == Blocks.FIRE || block == Blocks.WATER || block == Blocks.LAVA) return true;

        return false;
    }


}