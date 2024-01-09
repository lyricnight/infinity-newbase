package club.lyric.infinity.api.util.client.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * @author lyric
 * pro wrapper
 */
public class Bandhu extends Box {
    public Bandhu(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public Bandhu(BlockPos pos) {
        super(pos);
    }

    public Bandhu(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
    }

    public Bandhu(Vec3d pos1, Vec3d pos2) {
        super(pos1, pos2);
    }
}
