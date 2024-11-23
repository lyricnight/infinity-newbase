package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.api.util.minecraft.player.MovementPlayer;
import club.lyric.infinity.api.util.minecraft.player.PlayerPosition;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.impl.events.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.impl.modules.movement.NoAccelerate;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author 3arth
 */
//TODO: replace this
@SuppressWarnings("DataFlowIssue")
public final class Aura extends ModuleBase {

    public ModeSetting priority = new ModeSetting("Priority", this, "Armor", "Armor", "Health");
    public NumberSetting range = new NumberSetting("Range", this, 6.0f, 1.0f, 7.0f, 0.1f, "m");
    public BooleanSetting tele = new BooleanSetting("Teleport", false, this);
    public BooleanSetting require = new BooleanSetting("Require", true, this);
    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting animals = new BooleanSetting("Animals", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);
    public BooleanSetting cooldown = new BooleanSetting("Cooldown", false, this);
    public BooleanSetting sprint = new BooleanSetting("Sprint", false, this);

    public BooleanSetting render = new BooleanSetting("Render", false, this);

    @Nullable
    private Target target;

    public Aura() {
        super("Aura", "Hits people", Category.Combat);
    }

    @EventHandler()
    public void onInput(KeyPressEvent ignored) {
        if (target != null) MovementUtil.movementFix();
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            target = computeTarget(mc.player, mc.world);
            attack(target, mc.player, true);
        } else if (event.getStage() == 1) {
            attack(target, mc.player, false);
        }

        if (target != null && Managers.ANTICHEAT.isRotations()) {
            Vec3d playerPos = mc.player.getPos();

            float[] rotations = RotationUtils.getRotationsTo(playerPos, target.entity.getPos());

            Managers.ROTATIONS.setRotationPoint(new RotationPoint(rotations[0], rotations[1], 9, false));
        }
    }


    @Override
    public void onRender3D(MatrixStack matrixStack) {

        if (mc.player.isHolding(getSword())) return;

        if (target != null && render.value()) {

            Vec3d vec3D = Interpolation.interpolateEntity(target.entity());
            Color color = Managers.MODULES.getModuleFromClass(Colours.class).getColor();

            Render3DUtils.enable3D();
            matrixStack.push();

            Render3DUtils.drawBox(matrixStack, Interpolation.interpolatedBox(target.entity(), vec3D), new Color(color.getRed(), color.getGreen(), color.getBlue(), 76).getRGB());
            Render3DUtils.drawOutline(matrixStack, Interpolation.interpolatedBox(target.entity(), vec3D), new Color(color.getRed(), color.getGreen(), color.getBlue(), 255).getRGB());

            matrixStack.pop();
            Render3DUtils.disable3D();
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
        NoAccelerate.pauseGlobal = true;
        mc.interactionManager.attackEntity(mc.player, target.entity());
        mc.player.swingHand(Hand.MAIN_HAND);
        NoAccelerate.pauseGlobal = false;
        if (sprint.value()) {
            mc.player.setSprinting(true);
        }
    }

    @Nullable
    private Target computeTarget(ClientPlayerEntity player, ClientWorld level) {
        Target result = null;
        MovementPlayer teleportPlayer = null;
        for (Entity entity : level.getOtherEntities(null, PlayerUtils.getAABBOfRadius(player, range.getFValue()))) {
            if (entity == null || entity.isRemoved() || entity.getId() == player.getId() || !entity.isAttackable() || entity instanceof EndCrystalEntity || entity instanceof ExperienceOrbEntity || entity instanceof ArrowEntity || entity instanceof ItemEntity || entity instanceof ExperienceBottleEntity || player.getPassengerList().contains(entity) || entity.getPassengerList().contains(player) || entity instanceof PlayerEntity && Managers.FRIENDS.isFriend((PlayerEntity) entity) || !isValid(entity)) {
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
        return entity.getBoundingBox().squaredMagnitude(new Vec3d(position.x, position.y + yOffset, position.z)) < 6.0f;
    }

    public ModeSetting getPriority() {
        return priority;
    }

    public record Target(Entity entity, boolean inRangeForCurrentPos, boolean inRangeForLastPos,
                         @Nullable Vec3d teleportPos) {
        public boolean isBetterThan(@Nullable Target last, ClientPlayerEntity player, boolean recursive) {
            if (last == null || entity instanceof PlayerEntity && !(last.entity instanceof PlayerEntity) || last.teleportPos != null && teleportPos == null/* we prefer to not teleport*/) {
                return true;
            }

            if (last.entity instanceof PlayerEntity lastPlayer && entity instanceof PlayerEntity currentPlayer) {
                float lastHealth = PlayerUtils.getHealth(lastPlayer);
                float currentHealth = PlayerUtils.getHealth(currentPlayer);
                if (currentHealth <= 4.0f && currentHealth < lastHealth && Managers.MODULES.getModuleFromClass(Aura.class).getPriority().is("Health")) {
                    return true;
                }

                if (currentHealth < lastHealth && Managers.MODULES.getModuleFromClass(Aura.class).getPriority().is("Health")) {
                    return true;
                }

                float durability = getLowestDurability(lastPlayer);
                float lastDurability = getLowestDurability(currentPlayer);
                if (durability <= 0.3f && durability < lastDurability && Managers.MODULES.getModuleFromClass(Aura.class).getPriority().is("Armor")) {
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

    public Item getSword() {
        // DUMB AF LMFAO
        if (getItemCount(Items.NETHERITE_SWORD) == -1) return Items.DIAMOND_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1)
            return Items.IRON_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1 && getItemCount(Items.IRON_SWORD) == -1)
            return Items.STONE_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1 && getItemCount(Items.IRON_SWORD) == -1 && getItemCount(Items.STONE_SWORD) == -1)
            return Items.WOODEN_SWORD;

        return Items.NETHERITE_SWORD;
    }

    public int getItemCount(Item item) {
        if (mc.player == null) return 0;

        int counter = 0;

        for (int i = 0; i <= 44; ++i) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            counter += itemStack.getCount();
        }

        return counter;
    }


    private boolean isValid(Entity entity) {
        return entity instanceof PlayerEntity && players.value() || EntityUtils.isMob(entity) && mobs.value() || EntityUtils.isAnimal(entity) && animals.value();
    }
}
