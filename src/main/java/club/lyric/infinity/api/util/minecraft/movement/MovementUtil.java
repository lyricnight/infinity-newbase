package club.lyric.infinity.api.util.minecraft.movement;


import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author lyric
 * for movement.
 */

@SuppressWarnings("ConstantConditions")
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

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

    public static void createSpeed(double speed)
    {
        double[] directionSpeed = MovementUtil.directionSpeed(speed);

        mc.player.setVelocity(directionSpeed[0], mc.player.getVelocity().getY(), directionSpeed[1]);
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

    public static double calcEffects(double speed)
    {
        int amplifier;
        double i = speed;
        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
            i *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
            i /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return i;
    }

    public static double getJumpSpeed()
    {
        double defaultSpeed = 0.0;
        if (mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            int amplifier = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static double getSpeed() {
        return getSpeed(false);
    }

    public static double getSpeed(boolean slowness) {
        int amplifier;
        double defaultSpeed = 0.2873;
        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return defaultSpeed;
    }

    public static double getDistanceXZ() {
        double xDist = mc.player.getX() - mc.player.prevX;
        double zDist = mc.player.getX() - mc.player.prevZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

}
