package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author vasler
 */
public class Interpolation implements IMinecraft {

    public static Vec3d interpolateEntity(Entity entity) {
        double x;
        double y;
        double z;

        {
            x = interpolateLastTickPos(entity.getX(), entity.prevX) - getCameraPos().x;
            y = interpolateLastTickPos(entity.getY(), entity.prevY) - getCameraPos().y;
            z = interpolateLastTickPos(entity.getZ(), entity.prevZ) - getCameraPos().z;
        }

        return new Vec3d(x, y, z);
    }

    public static double interpolateLastTickPos(double pos, double lastPos) {
        return lastPos + (pos - lastPos) * mc.getTickDelta();
    }
    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }
}
