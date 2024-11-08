package club.lyric.infinity.api.util.minecraft.block;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyric
 */
public class BlockUtils implements IMinecraft {

    public static class Offsets {
        public final static BlockPos[] FACE_PLACE = new BlockPos[]{
                new BlockPos(1, 1, 0),
                new BlockPos(-1, 1, 0),
                new BlockPos(0, 1, 1),
                new BlockPos(0, 1, -1),
        };

        public final static BlockPos[] FEET_PLACE = new BlockPos[]{
                new BlockPos(1, 0, 0),
                new BlockPos(-1, 0, 0),
                new BlockPos(0, 0, 1),
                new BlockPos(0, 0, -1),
        };
    }

    public static double getDistanceSq(BlockPos pos) {
        return getDistanceSq(mc.player.getBlockPos(), pos);
    }

    public static double getDistanceSq(BlockPos from, BlockPos to) {
        return from.getSquaredDistanceFromCenter(to.getX(), to.getY(), to.getZ());
    }

    public static boolean canBreak(BlockPos pos) {
        return canBreak(mc.world.getBlockState(pos), pos);
    }

    public static boolean canBreak(BlockState state, BlockPos pos) {
        //noinspection deprecation
        return state.getHardness(mc.world, pos) != -1.0f && state.getBlock() != Blocks.AIR && !state.isLiquid();
    }

    public static boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static boolean isObby(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
    }

    public static boolean isBedrock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    public static boolean isEchest(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST;
    }

    public static boolean isSafe(BlockPos pos) {
        return isObby(pos) || isBedrock(pos) || isEchest(pos);
    }

    public static List<BlockPos> getSphere(Entity entity, float radius, boolean ignoreAir) {
        List<BlockPos> sphere = new ArrayList<>();

        BlockPos pos = entity.getBlockPos();

        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();

        int radiuss = (int) radius;

        for (int x = posX - radiuss; x <= posX + radius; x++) {
            for (int z = posZ - radiuss; z <= posZ + radius; z++) {
                for (int y = posY - radiuss; y < posY + radius; y++) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (ignoreAir && mc.world.getBlockState(position).getBlock() == Blocks.AIR) {
                            continue;
                        }
                        sphere.add(position);
                    }
                }
            }
        }

        return sphere;
    }
    public static boolean isInside(Entity entity, Box bb) {
        return mc.world.getBlockCollisions(entity, bb).iterator().hasNext();
    }
}
