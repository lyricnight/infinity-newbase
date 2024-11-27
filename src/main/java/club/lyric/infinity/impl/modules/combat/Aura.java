package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import club.lyric.infinity.api.util.minecraft.player.MovementPlayer;
import club.lyric.infinity.api.util.minecraft.player.PlayerPosition;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.impl.events.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

/**
 * @author 3arth
 * copied this as a PLACEHOLDER for TESTING -> INTENDED TO BE REPLACED BY CUSTOM MODULE.
 */

@SuppressWarnings("DataFlowIssue")
public final class Aura extends ModuleBase {
    public BooleanSetting tele = new BooleanSetting("Teleport", false, this);
    public BooleanSetting require = new BooleanSetting("Require", true, this);
    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting animals = new BooleanSetting("Animals", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);

    @Nullable
    private Target target;

    private boolean attacked;

    public Aura() {
        super("Aura", "Hits people", Category.Combat);
    }

    @EventHandler(priority = 1002)
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            attacked = false;
            target = computeTarget(mc.player, mc.world);
            attack(target, mc.player, true);
        } else if (event.getStage() == 1) {
            attack(target, mc.player, false);
        }
    }

    @Override
    public String moduleInformation() {
        if (target == null) {
            return Formatting.RED + "none";
        }
        return target.entity().getName().getString();
    }

    @Override
    public void onDisable() {
        target = null;
    }

    private void attack(@Nullable Target target, ClientPlayerEntity player, boolean preMotion) {
        if (target == null || attacked || player.isSpectator() || require.value() && !(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem || player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AxeItem) || player.getAttackCooldownProgress(0.5f) < 1.0f) {
            return;
        }
        if (preMotion) {
            if (target.inRangeForLastPos()) {
                if (!Managers.ANTICHEAT.isRotations()) {
                    executeAttack(target, player, mc.interactionManager);
                } else {
                    float[] rotations = RotationUtils.getRotationsTo(player.getPos(), target.entity.getPos());
                    Managers.ROTATIONS.setRotationPoint(
                            new RotationPoint(rotations[0], rotations[1], 10, false)
                    );
                }
            } else {
                if (!target.inRangeForCurrentPos() && target.teleportPos() != null) {
                    player.setPos(target.teleportPos.x, target.teleportPos.y, target.teleportPos.z);
                }

                if (Managers.ANTICHEAT.isRotations()) {
                    float[] rotations = RotationUtils.getRotationsTo(player.getPos(), target.entity.getPos());
                    Managers.ROTATIONS.setRotationPoint(
                            new RotationPoint(rotations[0], rotations[1], 10, false)
                    );
                }
            }
        } else {
            if (!Managers.ANTICHEAT.isRotations() && isInRange(target.entity, player.getPos(), player.getEyeHeight(EntityPose.STANDING))) {
                executeAttack(target, player, mc.interactionManager);
            }
        }
    }

    private void executeAttack(Target target, ClientPlayerEntity player, ClientPlayerInteractionManager world) {
        world.attackEntity(player, target.entity());
        player.swingHand(Hand.MAIN_HAND);
        attacked = true;
    }


    @Nullable
    private Target computeTarget(ClientPlayerEntity player, ClientWorld level) {
        Target result = null;
        MovementPlayer teleportPlayer = null;
        for (Entity entity : level.getOtherEntities(null, PlayerUtils.getAABBOfRadius(player, 8.0))) {
            if (entity == null || entity.isRemoved() || entity.getId() == player.getId() || !entity.isAttackable() || entity instanceof EndCrystalEntity || entity instanceof ExperienceOrbEntity || entity instanceof ArrowEntity || entity instanceof ItemEntity || entity instanceof ExperienceBottleEntity || player.getPassengerList().contains(entity) || entity.getPassengerList().contains(player) || entity instanceof PlayerEntity && Managers.FRIENDS.isFriend((PlayerEntity) entity) || !isValid(entity)) {
                continue;
            }

            Vec3d teleportPos = null;
            boolean inRangeForLastPos = false;
            boolean inRangeForCurrentPos = isInRange(entity, player.getEyePos(), 0.0);
            if (!inRangeForCurrentPos) {
                inRangeForLastPos = isInRange(entity, mc.player.getPos(), player.getEyeHeight(EntityPose.STANDING));
                if (!inRangeForLastPos && tele.value() && player.isOnGround()) {
                    PlayerPosition last = (PlayerPosition) player.getPos();
                    Vec3d own = last.add(0.0, player.getEyeHeight(EntityPose.STANDING), 0.0);
                    Vec3d bbCords = PlayerUtils.getBBCoords(entity.getBoundingBox(), own);
                    Vec3d vec = new Vec3d(bbCords.x, own.y, bbCords.z);
                    double cSq = MathHelper.square(6.0 - 1.0E-7);
                    double aSq = vec.squaredDistanceTo(bbCords);
                    double b = Math.sqrt(cSq - aSq);
                    Vec3d t = vec.add(own.subtract(vec).normalize().multiply(b));
                    teleportPos = t.subtract(0.0, player.getEyeHeight(EntityPose.STANDING), 0.0);
                    if (teleportPos.squaredDistanceTo(last) > MathHelper.square(PlayerUtils.getSpeed(player))) {
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
        return entity.getBoundingBox().squaredMagnitude(new Vec3d(position.x, position.y + yOffset, position.z)) < MathHelper.square(6.0f);
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

    private boolean isValid(Entity entity) {
        return entity instanceof PlayerEntity && players.value() || EntityUtils.isMob(entity) && mobs.value() || EntityUtils.isAnimal(entity) && animals.value();
    }
}
