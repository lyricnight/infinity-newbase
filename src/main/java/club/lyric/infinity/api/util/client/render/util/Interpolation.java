package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

/**
 * @author vasler
 */
public class Interpolation implements IMinecraft {

    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }
}
