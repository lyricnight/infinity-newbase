package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.impl.events.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;

import club.lyric.infinity.api.util.minecraft.player.MovementPlayer;
import club.lyric.infinity.api.util.minecraft.player.PlayerPosition;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.impl.events.render.Render3DEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

/**
 * @author 3arth
 */
@SuppressWarnings("DataFlowIssue")
public final class Aura extends ModuleBase {
    public NumberSetting range = new NumberSetting("Range", this, 6.0f, 1.0f, 7.0f, 0.1f);
    public BooleanSetting tele = new BooleanSetting("Teleport", false, this);

    public BooleanSetting require = new BooleanSetting("Require", true, this);

    public BooleanSetting entities = new BooleanSetting("AnyEntity", false, this);

    public BooleanSetting other = new BooleanSetting("Others", false, this);

    public BooleanSetting cooldown = new BooleanSetting("Cooldown", false, this);

    public BooleanSetting sprint = new BooleanSetting("Sprint", false, this);

    @Nullable
    private Target target;

    public Aura()
    {
        super("Aura", "Hits people", Category.Combat);
    }

    @EventHandler
    public void onInput(KeyPressEvent event) {
        movementFix();
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0)
        {
            target = computeTarget(mc.player, mc.world);
            attack(target, mc.player, true);
        }
        else if (event.getStage() == 1)
        {
            attack(target, mc.player, false);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (target != null) {

        }
    }

    @Override
    public String moduleInformation()
    {
        return target.entity().getName().getString();
    }

    @Override
    public void onDisable() {
        target = null;
    }

    private void attack(@Nullable Target target, ClientPlayerEntity player, boolean preMotion) {
        if (target == null || player.isSpectator() || cooldown.value() && !(player.getAttackCooldownProgress(0.5f) >= 1.0f) || preMotion && target.inRangeForCurrentPos() || !preMotion && !target.inRangeForCurrentPos() || require.value() && !(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem || player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AxeItem)) {
            return;
        }

        if (target.teleportPos() != null) {
            if (!preMotion) {
                return;
            }

            player.setPos(target.teleportPos().x, target.teleportPos().y, target.teleportPos().z);
        }

        if (cooldown.value() && !(mc.player.getAttackCooldownProgress(0) >= 1.0f)) return;

        mc.interactionManager.attackEntity(mc.player, target.entity());
        mc.player.swingHand(Hand.MAIN_HAND);

        if (sprint.value()) {
            mc.player.setSprinting(true);
        }
    }

    @Nullable
    private Target computeTarget(ClientPlayerEntity player, ClientWorld level) {
        Target result = null;
        MovementPlayer teleportPlayer = null;
        for (Entity entity : level.getOtherEntities(null, PlayerUtils.getAABBOfRadius(player, range.getFValue()))) {
            if (entity == null || entity.isRemoved() || entity.getId() == player.getId() || !entity.isAttackable() || entity instanceof EndCrystalEntity || entity instanceof ExperienceOrbEntity || entity instanceof ArrowEntity || entity instanceof ItemEntity || entity instanceof ExperienceBottleEntity || player.getPassengerList().contains(entity) || entity.getPassengerList().contains(player)  || entity instanceof LivingEntity && !(entity instanceof PlayerEntity) && !entities.value() || !(entity instanceof LivingEntity) && !other.value() || entity instanceof PlayerEntity && Managers.FRIENDS.isFriend((PlayerEntity) entity)) {
                continue;
            }

            Vec3d teleportPos = null;
            boolean inRangeForLastPos = false;
            boolean inRangeForCurrentPos = isInRange(entity, player.getEyePos(), 0.0);
            if (!inRangeForCurrentPos) {
                inRangeForLastPos = isInRange(entity, PlayerPosition.getPosition(), player.getEyeY());
                if (!inRangeForLastPos && tele.value() && player.isOnGround()) {
                    PlayerPosition last = PlayerPosition.getPosition();
                    Vec3d own = last.add(0.0, player.getEyeY(), 0.0);
                    Vec3d bbCords = PlayerUtils.getBBCoords(entity.getBoundingBox(), own);
                    Vec3d vec = new Vec3d(bbCords.x, own.y, bbCords.z);
                    double cSq = MathHelper.square(6.0 - 1.0E-7);
                    double aSq = vec.squaredDistanceTo(bbCords);
                    double b = Math.sqrt(cSq - aSq);
                    Vec3d t = vec.add(own.subtract(vec).normalize().multiply(b));
                    teleportPos = t.subtract(0.0, player.getStandingEyeHeight(), 0.0);
                    if (teleportPos.squaredDistanceTo(last) > MathHelper.square(PlayerUtils.getSpeed(mc.player))) {
                        continue;
                    }

                    if (teleportPlayer == null) {
                        teleportPlayer = new MovementPlayer(level);
                    }

                    teleportPlayer.setPosition(last);
                    Vec3d movementVector = teleportPos.subtract(last);
                    teleportPlayer.setVelocity(movementVector);
                    teleportPlayer.setOnGround(last.isOnGround());
                    teleportPlayer.move(MovementType.SELF, movementVector);
                    if (!teleportPlayer.getPos().equals(teleportPos)) {
                        continue;
                    }
                }
            }

            Target targetForEvaluation = new Target(entity, inRangeForCurrentPos, inRangeForLastPos, teleportPos);
            if (targetForEvaluation.isBetterThan(result, player, false)) {
                result = targetForEvaluation;
            }
        }

        return result;
    }

    private boolean isInRange(Entity entity, Vec3d position, double yOffset) {
        return entity.getBoundingBox().squaredMagnitude(new Vec3d(position.x, position.y + yOffset, position.z)) < ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
    }

    public record Target(Entity entity, boolean inRangeForCurrentPos, boolean inRangeForLastPos, @Nullable Vec3d teleportPos) {
        public boolean isBetterThan(@Nullable Target last, ClientPlayerEntity player, boolean recursive) {
            if (last == null || entity instanceof PlayerEntity && !(last.entity instanceof PlayerEntity) || last.teleportPos != null && teleportPos == null/* we prefer to not teleport*/) {
                return true;
            }

            if (last.entity instanceof PlayerEntity lastPlayer && entity instanceof PlayerEntity currentPlayer) {
                float lastHealth = PlayerUtils.getHealth(lastPlayer);
                float currentHealth = PlayerUtils.getHealth(currentPlayer);
                if (currentHealth <= 4.0f && currentHealth < lastHealth) {
                    return true;
                }

                if (currentHealth < lastHealth) {
                    return true;
                }

                float durability = getLowestDurability(lastPlayer);
                float lastDurability = getLowestDurability(currentPlayer);
                if (durability <= 0.3f && durability < lastDurability) {
                    return true;
                }
            }

            if (!recursive && last.isBetterThan(this, player, true)) {
                return false;
            }

            return entity.squaredDistanceTo(player) < last.entity.squaredDistanceTo(player);
        }

        private float getLowestDurability(PlayerEntity player) {
            float lowest = 1.0f;
            for (ItemStack stack : player.getInventory().armor) {
                if (!stack.isEmpty() && stack.getMaxDamage() > 0 && stack.getDamage() > 0) {
                    float durability = (float) (stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage();
                    if (durability < lowest) {
                        lowest = durability;
                    }
                }
            }

            return lowest;
        }
    }

    // maybe add to movementutils?
    public void movementFix() {
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

    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}
