package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.manager.Managers;
import com.google.common.collect.Streams;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* TODO: make calculated breaking smarter by: calculating enemy and own damage and finding the best possible crystal to break;
 * make placing modes like breaking: do the same shit i just said; ADD PLACING, add id predict*/

@Deprecated
@SuppressWarnings({"unused"})
public class AutoCrystal extends ModuleBase {
    // Entities
    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting animals = new BooleanSetting("Animals", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);
    public NumberSetting enemyRange = new NumberSetting("EnemyRange", this, 6.0f, 1.0f, 7.0f, 0.1f, "m");

    // Breaking
    public ModeSetting breaking = new ModeSetting("Breaking", this, "All", "Calculated", "All");
    public ModeSetting breaks = new ModeSetting("Break", this, "Vanilla", "Vanilla", "Packet");
    public NumberSetting hitRange = new NumberSetting("HitRange", this, 6.0f, 1.0f, 7.0f, 0.1f, "m");
    public NumberSetting hitDelay = new NumberSetting("HitDelay", this, 15.0f, 0.0f, 600.0f, 0.1f, "ms");
    public ModeSetting swing = new ModeSetting("Swing", this, "Vanilla", "Vanilla", "Packet", "None");
    public ModeSetting swingOn = new ModeSetting("SwingOn", this, "Both", "Both", "Place", "Break");
    public ModeSetting hands = new ModeSetting("SwingHand", this, "Main", "Main", "Off", "None");
    public BooleanSetting explosion = new BooleanSetting("Explosion", true, this);
    public BooleanSetting sound = new BooleanSetting("Sound", true, this);
    public NumberSetting tickExisted = new NumberSetting("TickExisted", this, 0.0f, 0.0f, 20.0f, 1.0f, " ticks");
    public BooleanSetting inhibit = new BooleanSetting("Inhibit", true, this);
    public NumberSetting timeout = new NumberSetting("Timeout", this, 500.0f, 0.0f, 2000.0f, 1.0f, "ms");

    // Placing
    public BooleanSetting place = new BooleanSetting("Place", false, this);
    public NumberSetting placeRange = new NumberSetting("PlaceRange", this, 6.0f, 1.0f, 7.0f, 0.1f, "m");
    public ModeSetting protocol = new ModeSetting("Protocol", this, "Current", "Current", "Past");
    public NumberSetting faceplaceHealth = new NumberSetting("FaceplaceHealth", this, 8.0f, 1.0f, 36.0f, 0.1f, "hp");
    public NumberSetting minimumDamage = new NumberSetting("MinimumDamage", this, 4.0f, 1.0f, 36.0f, 0.1f, "hp");
    public NumberSetting maxSelfDamage = new NumberSetting("MaxSelfDamage", this, 7.0f, 1.0f, 36.0f, 0.1f, "hp");
    public ModeSetting places = new ModeSetting("Placing", this, "Vanilla", "Vanilla", "Packet");

    // Render
    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(50, 50, 255)), true);
    private final StopWatch.Single breakTimer = new StopWatch.Single();
    private final Map<BlockPos, Long> ownCrystals = new HashMap<>();
    private final Map<Integer, StopWatch.Single> hitCrystals = new HashMap<>();
    private BlockPos renderPosition;
    ExecutorService service = Executors.newCachedThreadPool();
    private BlockPos placePositions;
    private PlayerEntity target;

    public AutoCrystal() {
        super("AutoCrystal", "automatically crystal", Category.COMBAT);
    }

    @Override
    public String moduleInformation() {

        Formatting grey = Formatting.GRAY;
        Formatting white = Formatting.WHITE;

        if (target == null) return "";

        return target.getName().getString();
    }

    @Override
    public void onEnable() {
        breakTimer.reset();
        ownCrystals.clear();

        // Debug calcs
    }

    @Override
    public void onUpdate() {
        List<LivingEntity> targets = Streams.stream(
                        mc.world.getEntities())
                .filter(e -> e instanceof PlayerEntity)
                .map(e -> (LivingEntity) e)
                .toList();

        List<EndCrystalEntity> near = Streams.stream(mc.world.getEntities()).filter(e -> e instanceof EndCrystalEntity).map(e -> (EndCrystalEntity) e).sorted(Comparator.comparing((mc.player::distanceTo))).toList();


        for (LivingEntity entity : targets)
        {
            target = (PlayerEntity) entity;
        }

        breakCrystals(near, targets);

        if (place.value())
        {
            placeCrystals(targets);
        }

        if (placePositions != null && Managers.ANTICHEAT.isRotations()) {
            Vec3d playerPos = mc.player.getPos();

            float[] rotations = RotationUtils.getRotationsTo(playerPos, placePositions.toCenterPos());

            Managers.ROTATIONS.setRotationPoint(new RotationPoint(rotations[0], rotations[1], 9, false));
        }
    }

    @EventHandler
    public void onInput(KeyPressEvent ignored) {
        if (placePositions != null) MovementUtil.movementFix();
    }


    public void breakCrystals(List<EndCrystalEntity> near, List<LivingEntity> targets) {
        // breaking
        if (mc.player != null) {
            for (EndCrystalEntity crystal : near) {

                if (crystal == null || crystal.age < tickExisted.getValue()) {
                    return;
                }

                if (breakTimer.hasBeen(hitDelay.getLValue())) {

                    if (breaking.is("Calculated") && !isOwn(crystal.getPos())) {
                        return;
                    }

                    //if (!isValid((Entity) targets)) return;

                    if (mc.player.distanceTo(crystal) >= hitRange.getValue() || !mc.world.getOtherEntities(null, new Box(crystal.getPos(), crystal.getPos()).expand(enemyRange.getFValue()), targets::contains).isEmpty()) {

                        if (inhibit.value()) {
                            StopWatch.Single timer = hitCrystals.get(Optional.of(crystal.getId()));
                            if (timer != null) {
                                if (!timer.hasBeen((timeout.getLValue()))) {
                                    continue;
                                }
                                hitCrystals.remove(Optional.of(crystal.getId()));
                            }
                        }

                        String information = "Breaking";

                        service.submit(() -> {
                            hitCrystals.put(Integer.valueOf(crystal.getId()), new StopWatch.Single());

                            switch (breaks.getMode()) {
                                case "Vanilla" -> mc.interactionManager.attackEntity(mc.player, crystal);
                                case "Packet" ->
                                        sendUnsafe(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
                            }

                            if (swingOn.is("Both") && swingOn.is("Break")) {
                                switch (hands.getMode()) {
                                    case "Main" -> swingType(Hand.MAIN_HAND);
                                    case "Off" -> swingType(Hand.OFF_HAND);
                                }
                            }

                            breakTimer.reset();
                        });
                    }
                }
            }
        }
    }

    public void placeCrystals(List<LivingEntity> targets) {
        if (mc.player != null) {

            for (LivingEntity crystal : targets) {

                float maxDamage = Math.ceil(crystal.getHealth() + crystal.getAbsorptionAmount()) > faceplaceHealth.getFValue() ? minimumDamage.getFValue() : 2.0f;

                for (BlockPos pos : BlockUtils.getSphere(mc.player, placeRange.getFValue(), false)) {

                    if (!canPlaceCrystal(pos, protocol.is("Current"))) continue;

                    float targetDamage = calculate(pos, crystal);

                    if (targetDamage <= maxDamage) continue;

                    float selfDamage = calculate(pos, mc.player);

                    if (Math.ceil(mc.player.getHealth() + mc.player.getAbsorptionAmount()) - 0.5 <= selfDamage || maxSelfDamage.getFValue() <= selfDamage || targetDamage <= selfDamage)
                        continue;

                    maxDamage = targetDamage;

                    placePositions = pos;

                }

            }
            if (mc.crosshairTarget instanceof BlockHitResult hit) {
                final BlockPos block = hit.getBlockPos();
                renderPosition = block;
                place(block);
            }
        }
    }

    @Override
    public void onTickPost() {
        List<BlockPos> toRemove = new ArrayList<>();
        ownCrystals.forEach((key, val) -> {
            if (System.currentTimeMillis() - val >= 5000)
                toRemove.add(key);
        });
        toRemove.forEach(ownCrystals::remove);
    }

    public void swingType(Hand hand) {
        switch (swing.getMode()) {
            case "Vanilla" -> mc.player.swingHand(hand);
            case "Packet" -> mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {

        if (renderPosition == null) return;

        Box bb = Interpolation.interpolatePos(renderPosition, 1.0f);

        Render3DUtils.enable3D();
        matrixStack.push();

        Render3DUtils.drawBox(matrixStack, bb, ColorUtils.alpha(color.getColor(), 70).getRGB());
        Render3DUtils.drawOutline(matrixStack, bb, ColorUtils.alpha(color.getColor(), 255).getRGB());

        matrixStack.pop();
        Render3DUtils.disable3D();
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (explosion.value()) {
            if (event.getPacket() instanceof ExplosionS2CPacket explosionPacket) {
                for (Entity ent : mc.world.getEntities()) {
                    if (ent == null) {
                        return;
                    }
                    if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(explosionPacket.center().x, explosionPacket.center().y, explosionPacket.center().z) <= 6.0d) {
                        int entity = crystal.getId();
                        mc.executeSync(() -> {
                            mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                            mc.world.removeBlockEntity(crystal.getBlockPos());
                        });
                    }
                }
            }
        }

        if (sound.value()) {
            if (event.getPacket() instanceof PlaySoundS2CPacket soundPacket) {
                if (soundPacket.getCategory() == SoundCategory.BLOCKS && soundPacket.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity ent : mc.world.getEntities()) {
                        if (ent == null) {
                            return;
                        }
                        if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) < 36.0d) {
                            int entity = crystal.getId();

                            mc.executeSync(() -> {
                                mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                                mc.world.removeBlockEntity(crystal.getBlockPos());
                            });
                        }
                    }
                }
            }
        }

        if (event.getPacket() instanceof EntitySpawnS2CPacket spawnPacket) {
            for (Entity ent : mc.world.getEntities()) {
                if (ent == null) return;

                if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ()) <= hitRange.getFValue()) {
                    int entity = crystal.getId();
                    mc.executeSync(() -> {
                        mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                        mc.world.removeBlockEntity(crystal.getBlockPos());
                    });
                }
            }
        }

        if (event.packet instanceof PlayerInteractBlockC2SPacket packet) {
            if (isOwn(packet.getBlockHitResult().getBlockPos().up()))
                ownCrystals.remove(packet.getBlockHitResult().getBlockPos().up());

            ownCrystals.put(packet.getBlockHitResult().getBlockPos().up(), Long.valueOf(System.currentTimeMillis()));
        }
    }

    private boolean isOwn(Vec3d vec) {
        return isOwn(BlockPos.ofFloored(vec));
    }

    // pasted form blackout
    private boolean isOwn(BlockPos pos) {
        for (Map.Entry<BlockPos, Long> entry : ownCrystals.entrySet()) {
            if (entry.getKey().equals(pos)) return true;
        }
        return false;
    }

    private boolean isValid(Entity entity) {
        return entity instanceof PlayerEntity && players.value() || EntityUtils.isMob(entity) && mobs.value() || EntityUtils.isAnimal(entity) && animals.value();
    }

    private void place(BlockPos pos) {
        Hand hand = null;

        if (pos == null) return;

        Direction dir = getDirection(pos);

        BlockHitResult result = new BlockHitResult(pos.toCenterPos(), dir, pos, false);

        switch (hands.getMode()) {
            case "Main" -> hand = Hand.MAIN_HAND;
            case "Off" -> hand = Hand.OFF_HAND;
        }

        Hand finalHand = hand;
        switch (places.getMode()) {
            case "Vanilla" -> mc.interactionManager.interactBlock(mc.player, hand, result);
            case "Packet" -> sendSeq(id -> new PlayerInteractBlockC2SPacket(finalHand, result, id));
        }
        Infinity.LOGGER.info("its trying to place");

        if (swingOn.is("Both") && swingOn.is("Place")) {
            swingType(hand);
        }
    }

    private Direction getDirection(BlockPos blockPos) {

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if (mc.world.isInBuildLimit(blockPos)) {
            return Direction.DOWN;
        }

        BlockHitResult result = mc.world.raycast(new RaycastContext(mc.player.getEyePos(), new Vec3d(x + 0.5, y + 0.5, z + 0.5), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

        if (result != null && result.getType() == HitResult.Type.BLOCK) {
            return result.getSide();
        }

        return Direction.UP;
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean newVer) {
        World world = mc.world;
        BlockPos boost = pos.up();

        if (world.getBlockState(pos).getBlock() != Blocks.BEDROCK && world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }

        BlockPos boost2 = pos.up(2);

        if ((!world.getBlockState(boost).isAir() || !world.getBlockState(boost2).isAir()) && !newVer) {
            return false;
        }

        for (Entity entity : world.getOtherEntities(null, new Box(boost))) {
            if (entity.isRemoved() || entity instanceof EndCrystalEntity) {
                continue;
            }
            return false;
        }

        for (Entity entity : world.getOtherEntities(null, new Box(boost2))) {
            if (entity.isRemoved() || entity instanceof EndCrystalEntity) {
                continue;
            }
            return false;
        }

        return true;
    }

    public static float calculate(BlockPos pos, LivingEntity entity) {
        double distance = entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) / 12 * 12;

        if (distance > 1.0) return 0.0f;

        BlockPos entityPos = entity.getBlockPos();
        Vec3d targetPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

        double density = entityPos.isWithinDistance(targetPos, 12.0) ? 0.0 : 1.0 - entity.getWorld().getBlockState(entityPos).getCollisionShape(entity.getWorld(), entityPos).getBoundingBox().getAverageSideLength();
        double densityDistance = (1.0 - distance) * density;

        float damage = getDifficultyMultiplier((float) ((densityDistance * densityDistance + distance) / 2.0 * 85.0));

//        Explosion explosion = new Explosion(mc.world, null, pos.getX(), pos.getY(), pos.getZ(), 6.0f, false, Explosion.DestructionType.DESTROY);
//
//        if (explosion.getCausingEntity() == null) return 0.0f;

        damage = applyArmorCalculations(damage, entity, mc.world.getDamageSources().explosion(null));
        damage = applyPotionEffects(damage, entity);

        return Math.max(damage, 0.0f);
    }

    private static float getDifficultyMultiplier(float distance) {
        return switch (mc.world.getDifficulty()) {
            case PEACEFUL -> 0.0f;
            case EASY -> Math.min(distance / 2.0f + 1.0f, distance);
            case NORMAL -> distance;
            case HARD -> distance * 3.0f / 2.0f;
        };
    }

    private static float applyArmorCalculations(float damage, LivingEntity entity, DamageSource source) {
        damage = DamageUtil.getDamageLeft(entity, damage, source, (float) entity.getArmor(), (float) entity.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue());

        int enchantmentModifier = (int) EnchantmentHelper.getProtectionAmount(mc.getServer().getWorld(World.OVERWORLD), entity, source);

        if (enchantmentModifier > 0) damage = DamageUtil.getInflictedDamage(damage, (float) enchantmentModifier);

        return damage;
    }

    private static float applyPotionEffects(float damage, LivingEntity entity) {
        if (entity.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int resistanceAmplifier = entity.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier();
            damage = damage * (25 - (resistanceAmplifier + 1) * 5) / 25.0f;
        }
        return damage;
    }
}
