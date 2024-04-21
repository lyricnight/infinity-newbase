package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * @author vasler
 */
public class Interpolation implements IMinecraft
{
    public static Vec3d getCameraPos()
    {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) return Vec3d.ZERO;

        return camera.getPos();
    }

    public static Box interpolatePos(BlockPos pos)
    {
        return interpolatePos(pos, 1.0f);
    }

    public static Box interpolatePos(BlockPos pos, float height)
    {
        return new Box(pos.getX() - getCameraPos().x, pos.getY() - getCameraPos().y, pos.getZ() - getCameraPos().z, pos.getX() - getCameraPos().x + 1, pos.getY() - getCameraPos().y + height, pos.getZ() - getCameraPos().z + 1);
    }

}
