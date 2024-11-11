package club.lyric.infinity.api.util.minecraft.player;


import club.lyric.infinity.api.ducks.IVec3d;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author various
 */

public class PlayerUtils implements IMinecraft {
    private static final List<Block> burrowList = Arrays.asList(
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.BEACON,
            Blocks.PISTON,
            Blocks.REDSTONE_BLOCK,
            Blocks.ENCHANTING_TABLE,
            Blocks.ANVIL
    );

    private static final Map<StatusEffect, String> statusEffectNames = new Reference2ObjectOpenHashMap<>(16);

    public static float getHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }


    public static String get(StatusEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringHelper.stripTextFormat(effect1.getName().getString()));
    }

    public static String getPotionDurationString(StatusEffectInstance effect) {
        if (effect.isInfinite()) {
            return "∞∞:∞∞";
        } else {
            DecimalFormat minuteFormat = new DecimalFormat("0");
            DecimalFormat secondsFormat = new DecimalFormat("00");
            long durationInTicks = effect.getDuration();
            float durationInSeconds = (float) durationInTicks / 20.0F;
            long minutes = TimeUnit.SECONDS.toMinutes((long) durationInSeconds);
            long seconds = TimeUnit.SECONDS.toSeconds((long) durationInSeconds) % 60L;
            return minuteFormat.format(minutes) + ":" + secondsFormat.format(seconds);
        }
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

    public static boolean isInBurrow(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        return isBurrow(pos, player) || isBurrow(pos.up(), player);
    }

    public static boolean isBurrow(BlockPos pos, PlayerEntity player) {
        BlockState state = mc.world.getBlockState(pos);
        return burrowList.contains(state.getBlock()) && state.getCollisionShape(mc.world, pos).getBoundingBox().offset(pos).maxY > player.getY();
    }

    public static float getPlayerHealth() {
        return mc.player.getAbsorptionAmount() + mc.player.getHealth();
    }

    public static boolean isTrapped(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        return !mc.world.getBlockState(pos.up(2)).isReplaceable();
    }

    public static boolean isInPhase(PlayerEntity target) {
        return mc.world.getBlockState(target.getBlockPos()).getBlock() == Blocks.AIR;
    }

    public static Vec3d predict(PlayerEntity player) {
        Vec3d movement = new Vec3d(player.getVelocity().x, player.getVelocity().y, player.getVelocity().z);
        collide(movement, player);
        return player.getPos().add(movement);
    }

    private static void collide(Vec3d movement, PlayerEntity player) {
        set(movement, player.adjustMovementForSneaking(movement, MovementType.SELF));
        set(movement, adjustMovementForCollisions(player, movement));
    }

    private static void set(Vec3d vec, Vec3d to) {
        ((IVec3d)vec).infinity$set(to.x, to.y, to.z);
    }

    public static Vec3d adjustMovementForCollisions(Entity entity, Vec3d movement) {
        Box box = entity.getBoundingBox();
        List<VoxelShape> list = entity.getWorld().getEntityCollisions(entity, box.stretch(movement));
        Vec3d vec3d = movement.lengthSquared() == 0.0 ? movement : Entity.adjustMovementForCollisions(entity, movement, box, entity.getWorld(), list);
        boolean bl = movement.x != vec3d.x;
        boolean bl2 = movement.y != vec3d.y;
        boolean bl3 = movement.z != vec3d.z;
        boolean bl5 = entity.isOnGround() || bl2 && movement.y < 0.0;
        if (entity.getStepHeight() > 0.0f && bl5 && (bl || bl3)) {
            Vec3d vec3d4;
            Vec3d vec3d2 = Entity.adjustMovementForCollisions(entity, new Vec3d(movement.x, entity.getStepHeight(), movement.z), box, entity.getWorld(), list);
            Vec3d vec3d3 = Entity.adjustMovementForCollisions(entity, new Vec3d(0.0, entity.getStepHeight(), 0.0), box.stretch(movement.x, 0.0, movement.z), entity.getWorld(), list);
            if (vec3d3.y < entity.getStepHeight() && (vec3d4 = Entity.adjustMovementForCollisions(entity, new Vec3d(movement.x, 0.0, movement.z), box.offset(vec3d3), entity.getWorld(), list).add(vec3d3)).horizontalLengthSquared() > vec3d2.horizontalLengthSquared()) {
                vec3d2 = vec3d4;
            }
            if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
                return vec3d2.add(Entity.adjustMovementForCollisions(entity, new Vec3d(0.0, -vec3d2.y + movement.y, 0.0), box.offset(vec3d2), entity.getWorld(), list));
            }
        }
        return vec3d;
    }
}
