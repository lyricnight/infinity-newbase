package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringHelper;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.minecraft.util.math.MathHelper.floor;

public class PlayerUtils implements IMinecraft {

    private static final Map<StatusEffect, String> statusEffectNames = new Reference2ObjectOpenHashMap<>(16);

    public static float getHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }


    public static String get(StatusEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringHelper.stripTextFormat(effect1.getName().getString()));
    }

    public static String getPotionDurationString(StatusEffectInstance effect)
    {
        if (effect.isInfinite())
        {
            return "∞∞:∞∞";
        }
        else
        {
            DecimalFormat minuteFormat = new DecimalFormat("0");
            DecimalFormat secondsFormat = new DecimalFormat("00");
            long durationInTicks = effect.getDuration();
            float durationInSeconds = (float)durationInTicks / 20.0F;
            long minutes = TimeUnit.SECONDS.toMinutes((long)durationInSeconds);
            long seconds = TimeUnit.SECONDS.toSeconds((long)durationInSeconds) % 60L;
            return minuteFormat.format(minutes) + ":" + secondsFormat.format(seconds);
        }
    }

    //TODO: fix this returning false when we drop a block in phase
    @SuppressWarnings("all")
    public static boolean isPhasing() {
        if(mc.player == null || mc.world == null) return false;
        Box box = mc.player.getBoundingBox();
        for (int x = floor(box.minX); x < floor(box.maxX) + 1; x++) {
            for (int y = floor(box.minY); y < floor(box.maxY) + 1; y++) {
                for (int z = floor(box.minZ); z < floor(box.maxZ) + 1; z++) {
                    if (mc.world.getBlockState(new BlockPos(x, y, z)).blocksMovement()) {
                        if (box.intersects(new Box(x, y, z, x + 1, y + 1, z + 1))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Box getAABBOfRadius(Entity entity, double radius) {
        return new Box(Math.floor(entity.getX() - radius), Math.floor(entity.getY() - radius), Math.floor(entity.getZ() - radius), Math.floor(entity.getX() + radius), Math.floor(entity.getY() + radius), Math.floor(entity.getZ() + radius));
    }

    public static Vec3d getBBCoords(Box bb, Vec3d from) {
        double x = bb.minX - from.x > from.x - bb.maxX ? bb.minX : bb.maxX;
        double y = bb.minY - from.y > from.y - bb.maxY ? bb.minY : bb.maxY;
        double z = bb.minZ - from.z > from.z - bb.maxZ ? bb.minZ : bb.maxZ;
        return new Vec3d(x, y, z);
    }

    public static double getSpeed(PlayerEntity player) {
        double speed = 0.287;
        var speedEffect = player.getStatusEffect(StatusEffects.SPEED);
        if (speedEffect != null) {
            speed *= 1.0 + 0.2 * (speedEffect.getAmplifier() + 1);
        }

        var slowEffect = player.getStatusEffect(StatusEffects.SLOWNESS);
        if (slowEffect != null) {
            speed /= 1.0 + 0.2 * (slowEffect.getAmplifier() + 1);
        }

        return speed;
    }
}
