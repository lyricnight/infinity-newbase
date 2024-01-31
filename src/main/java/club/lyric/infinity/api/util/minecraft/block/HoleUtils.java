package club.lyric.infinity.api.util.minecraft.block;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.util.math.BlockPos;

/**
 * @author lyric
 */

public class HoleUtils implements IMinecraft {
    public final static BlockPos[] holeOffsets = new BlockPos[] {
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(0, -1, 0)
    };
}
