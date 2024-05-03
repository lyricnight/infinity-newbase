package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("DataFlowIssue")
public final class MovementManager implements IMinecraft {

    public void movementFix()
    {

        if (!AntiCheat.getFix()) return;

        float forward = mc.player.input.movementForward;
        float sideways = mc.player.input.movementSideways;
        float delta = (mc.player.getYaw()) * MathHelper.RADIANS_PER_DEGREE;
        float cos = MathHelper.cos(delta);
        float sin = MathHelper.sin(delta);
        final float strafe = mc.player.input.movementSideways;
        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(mc.player.bodyYaw, forward, strafe)));
        if (forward == 0 && strafe == 0) {
            return;
        }
        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;
        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = Math.round(sideways * cos - forward * sin);
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }
        mc.player.input.movementForward = closestForward;
        mc.player.input.movementSideways = closestStrafe;
    }

    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing)
    {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

}
