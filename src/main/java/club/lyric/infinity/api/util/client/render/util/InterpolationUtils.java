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
public class InterpolationUtils implements IMinecraft {
    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) return Vec3d.ZERO;

        return camera.getPos();
    }

    public static Vec3d interpolateEntity(Entity entity) {
        double x;
        double y;
        double z;
        {
            x = entity.prevX + (entity.getX() - entity.prevX) * mc.getRenderTickCounter().getTickDelta(false) - getCameraPos().x;
            y = entity.prevY + (entity.getY() - entity.prevY) * mc.getRenderTickCounter().getTickDelta(false) - getCameraPos().y;
            z = entity.prevZ + (entity.getZ() - entity.prevZ) * mc.getRenderTickCounter().getTickDelta(false) - getCameraPos().z;
        }

        return new Vec3d(x, y, z);
    }

    public static Box interpolatePos(BlockPos pos, float height) {
        return new Box(pos.getX() - getCameraPos().x, pos.getY() - getCameraPos().y, pos.getZ() - getCameraPos().z, pos.getX() - getCameraPos().x + 1, pos.getY() - getCameraPos().y + height, pos.getZ() - getCameraPos().z + 1);
    }

    public static Box interpolatedBox(Entity entity, Vec3d vec3d) {
        return new Box(0.0, 0.0, 0.0, entity.getWidth(), entity.getHeight(), entity.getWidth()).offset(vec3d.x - (double) (entity.getWidth() / 2.0F), vec3d.y, vec3d.z - (double) (entity.getWidth() / 2.0F));
    }

    public static float interpolateFloat(float prev, float value, float factor) {
        return prev + ((value - prev) * factor);
    }
}
