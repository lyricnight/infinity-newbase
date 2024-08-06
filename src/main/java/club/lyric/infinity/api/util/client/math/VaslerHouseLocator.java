package club.lyric.infinity.api.util.client.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * @author lyric
 * great class
 */
public class VaslerHouseLocator extends BlockPos {
    public VaslerHouseLocator(int i, int j, int k) {
        super(i, j, k);
    }

    public VaslerHouseLocator(Vec3i pos) {
        super(pos);
    }

    public BlockPos get()
    {
        return new BlockPos(this.getX(), this.getY(), this.getX());
    }
}
