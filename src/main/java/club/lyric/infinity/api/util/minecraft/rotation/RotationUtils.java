package club.lyric.infinity.api.util.minecraft.rotation;

import club.lyric.infinity.api.util.client.math.apache.ApacheMath;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class RotationUtils implements IMinecraft {
    public static Vec2f getRotationTo(Vec3d posTo, Vec3d posFrom) {
        return getRotationFromVec(posTo.subtract(posFrom));
    }

    public static double normalizeAngle(Double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double xz = ApacheMath.hypot(vec.x, vec.z);
        float yaw = (float) normalizeAngle(ApacheMath.toDegrees(ApacheMath.atan2(vec.z, vec.x)) - 90.0);
        float pitch = (float) normalizeAngle(ApacheMath.toDegrees(-ApacheMath.atan2(vec.y, xz)));
        return new Vec2f(yaw, pitch);
    }
}
