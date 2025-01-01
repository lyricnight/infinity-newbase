package club.lyric.infinity.api.util.minecraft.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;

import java.util.List;
import java.util.function.Function;

public class MovementPlayer extends Fake {
    private final Function<Vec3d, Vec3d> moveCallback = Function.identity();

    public MovementPlayer(ClientWorld level) {
        super(level);
    }
    @Override
    public void copyPositionAndRotation(Entity entity) {
        super.copyPositionAndRotation(entity);
        this.setOnGround(entity.isOnGround());
        this.verticalCollision = entity.verticalCollision;
        this.groundCollision = entity.groundCollision;
        this.horizontalCollision = entity.horizontalCollision;
    }

    @Override
    public void move(MovementType moverType, Vec3d motion) {
        motion = moveCallback.apply(motion);
        if (this.movementMultiplier.lengthSquared() > 1.0E-7) {
            motion = motion.multiply(this.movementMultiplier);
            this.movementMultiplier = Vec3d.ZERO;
            this.setVelocity(Vec3d.ZERO);
        }

        Vec3d collideVec;
        double collideDistance;
        if ((collideDistance = (collideVec = this.collide(motion = this.adjustMovementForSneaking(motion, moverType))).lengthSquared()) > 1.0E-7) {
            if (this.fallDistance != 0.0f && collideDistance >= 1.0 && this.getWorld().raycast(new RaycastContext(this.getPos(), this.getPos().add(collideVec), RaycastContext.ShapeType.FALLDAMAGE_RESETTING, RaycastContext.FluidHandling.WATER, this)).getType() != HitResult.Type.MISS) {
                this.onLanding();
            }

            this.setPos(this.getX() + collideVec.x, this.getY() + collideVec.y, this.getZ() + collideVec.z);
        }

        boolean xDiff = !MathHelper.approximatelyEquals(motion.x, collideVec.x);
        boolean zDiff = !MathHelper.approximatelyEquals(motion.z, collideVec.z);
        this.horizontalCollision = xDiff || zDiff;
        this.verticalCollision = motion.y != collideVec.y;
        this.groundCollision = this.verticalCollision && motion.y < 0.0;
        this.collidedSoftly = this.horizontalCollision && this.hasCollidedSoftly(collideVec);
        this.setOnGround(this.groundCollision);

        @SuppressWarnings("deprecation") BlockPos blockPos = this.getLandingPos();
        BlockState onPosState = this.getWorld().getBlockState(blockPos);
        updateFallDistance(collideVec.y, this.isOnGround());
        if (this.horizontalCollision) {
            Vec3d deltaMovement = this.getVelocity();
            this.setVelocity(xDiff ? 0.0 : deltaMovement.x, deltaMovement.y, zDiff ? 0.0 : deltaMovement.z);
        }

        Block block = onPosState.getBlock();
        if (motion.y != collideVec.y) {
            block.onEntityLand(this.getWorld(), this);
        }

        float blockSpeedFactor = this.getVelocityMultiplier();
        this.setVelocity(this.getVelocity().multiply(blockSpeedFactor, 1.0, blockSpeedFactor));
    }

    @Override
    public Vec3d adjustMovementForSneaking(Vec3d vec3, MovementType moverType) {
        // make public
        return super.adjustMovementForSneaking(vec3, moverType);
    }

    public Vec3d collide(Vec3d motion) {
        Box bb = this.getBoundingBox();
        List<VoxelShape> list = this.getWorld().getEntityCollisions(this, bb.stretch(motion));
        Vec3d result = motion.lengthSquared() == 0.0 ? motion : Entity.adjustMovementForCollisions(this, motion, bb, this.getWorld(), list);
        boolean xDiff = motion.x != result.x;
        boolean yDiff = motion.y != result.y;
        boolean zDiff = motion.z != result.z;
        boolean onGround = this.isOnGround() || yDiff && motion.y < 0.0;
        if (this.getStepHeight() > 0.0f && onGround && (xDiff || zDiff)) {
            Vec3d afterEntityCollisions = Entity.adjustMovementForCollisions(this, new Vec3d(motion.x, this.getStepHeight(), motion.z), bb, this.getWorld(), list);
            Vec3d stepCollisions = Entity.adjustMovementForCollisions(this, new Vec3d(0.0, this.getStepHeight(), 0.0), bb.stretch(motion.x, 0.0, motion.z), this.getWorld(), list);
            if (stepCollisions.y < this.getStepHeight()) {
                Vec3d afterMotion = Entity.adjustMovementForCollisions(this, new Vec3d(motion.x, 0.0, motion.z), bb.offset(stepCollisions), this.getWorld(), list).add(stepCollisions);
                if (afterMotion.horizontalLengthSquared() > afterEntityCollisions.horizontalLengthSquared()) {
                    afterEntityCollisions = afterMotion;
                }
            }

            if (afterEntityCollisions.horizontalLengthSquared() > result.horizontalLengthSquared()) {
                return afterEntityCollisions.add(Entity.adjustMovementForCollisions(this, new Vec3d(0.0, -afterEntityCollisions.y + motion.y, 0.0), bb.offset(afterEntityCollisions), this.getWorld(), list));
            }
        }

        return result;
    }

    private void updateFallDistance(double fallDistanceThisTick, boolean onGround) {
        if (onGround) {
            this.onLanding();
        } else if (fallDistanceThisTick < 0.0) {
            this.fallDistance -= (float) fallDistanceThisTick;
        }
    }

}
