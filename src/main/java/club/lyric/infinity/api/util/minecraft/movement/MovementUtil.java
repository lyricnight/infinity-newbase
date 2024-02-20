package club.lyric.infinity.api.util.minecraft.movement;

import club.lyric.infinity.api.util.client.math.apache.ApacheMath;
import club.lyric.infinity.api.util.minecraft.IMinecraft;

/**
 * @author lyric
 * for movement.
 */

public class MovementUtil implements IMinecraft {

    /**
     * speed
     * @param speed - speed of player
     * @return directional speed
     */
    @SuppressWarnings("all")
    public static double[] directionSpeed(double speed) {
        float forward = mc.player.input.movementForward;
        float side = mc.player.input.movementSideways;
        float yaw = mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getTickDelta();

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = ApacheMath.sin(ApacheMath.toRadians(yaw + 90));
        final double cos = ApacheMath.cos(ApacheMath.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

    /**
     * check if movement keys are down
     * @return above
     */
    public static boolean movement()
    {
        //noinspection DataFlowIssue
        return mc.player.input.pressingLeft || mc.player.input.pressingRight || mc.player.input.pressingBack || mc.player.input.pressingForward || mc.player.input.sneaking;
    }
}
