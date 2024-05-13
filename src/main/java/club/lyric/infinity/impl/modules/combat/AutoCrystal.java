package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* TODO: make calculated breaking smarter by: calculating enemy and own damage and finding the best possible crystal to break;
 * make placing modes like breaking: do the same shit i just said; ADD PLACING, add id predict*/

@SuppressWarnings({"unused", "ConstantConditions"})
public class AutoCrystal extends ModuleBase {
    // Entities
    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting animals = new BooleanSetting("Animals", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);
    public NumberSetting enemyRange = new NumberSetting("EnemyRange", this, 6.0f, 1.0f, 7.0f, 0.1f);
    // Breaking
    public ModeSetting breaking = new ModeSetting("Breaking", this, "All", "Calculated", "All");
    public ModeSetting breaks = new ModeSetting("Break", this, "Vanilla", "Vanilla", "Packet");
    public NumberSetting hitRange = new NumberSetting("HitRange", this, 6.0f, 1.0f, 7.0f, 0.1f);
    public NumberSetting hitDelay = new NumberSetting("HitDelay", this, 15.0f, 0.0f, 600.0f, 0.1f);
    public ModeSetting swing = new ModeSetting("Swing", this, "Vanilla", "Vanilla", "Packet", "None");
    public ModeSetting swingOn = new ModeSetting("SwingOn", this, "Both", "Both", "Place", "Break");
    public ModeSetting hands = new ModeSetting("SwingHand", this, "Main", "Main", "Off", "None");
    public BooleanSetting explosion = new BooleanSetting("Explosion", true, this);
    public BooleanSetting sound = new BooleanSetting("Sound", true, this);
    public NumberSetting tickExisted = new NumberSetting("TickExisted", this, 0.0f, 0.0f, 20.0f, 1.0f);
    public BooleanSetting inhibit = new BooleanSetting("Inhibit", true, this);
    public NumberSetting timeout = new NumberSetting("Timeout", this, 500.0f, 0.0f, 2000.0f, 1.0f);
    private final StopWatch.Single breakTimer = new StopWatch.Single();
    private final Map<BlockPos, Long> ownCrystals = new HashMap<>();
    private final Map<Integer, StopWatch.Single> hitCrystals = new HashMap<>();
    ExecutorService service = Executors.newCachedThreadPool();

    public AutoCrystal() {
        super("AutoCrystal", "automatically crystal", Category.Combat);
    }

    @Override
    public String moduleInformation() {
        return "GG";
    }

    @Override
    public void onEnable() {
        breakTimer.reset();
        ownCrystals.clear();
    }

    @Override
    public void onUpdate() {
        List<LivingEntity> targets = Streams.stream(
                        mc.world.getEntities())
                .filter(e -> e instanceof PlayerEntity)
                .map(e -> (LivingEntity) e)
                .toList();

        List<EndCrystalEntity> near = Streams.stream
                        (mc.world.getEntities())
                .filter(e -> e instanceof EndCrystalEntity)
                .map(e -> (EndCrystalEntity) e)
                .sorted(Comparator.comparing(mc.player::distanceTo))
                .toList();

        breakCrystals(near, targets);
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
                            StopWatch.Single timer = hitCrystals.get(crystal.getId());
                            if (timer != null) {
                                if (!timer.hasBeen((timeout.getLValue()))) {
                                    continue;
                                }
                                hitCrystals.remove(crystal.getId());
                            }
                        }

                        String information = "Breaking";

                        service.submit(() -> {
                            hitCrystals.put(crystal.getId(), new StopWatch.Single());

                            switch (breaks.getMode()) {
                                case "Vanilla" -> mc.interactionManager.attackEntity(mc.player, crystal);
                                case "Packet" ->
                                        send(PlayerInteractEntityC2SPacket.attack(mc.player, mc.player.isSneaking()));
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

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (explosion.value()) {
            if (event.getPacket() instanceof ExplosionS2CPacket explosionPacket) {
                for (Entity ent : mc.world.getEntities()) {
                    if (ent == null) {
                        return;
                    }
                    if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(explosionPacket.getX(), explosionPacket.getY(), explosionPacket.getZ()) <= 6.0d)
                    {
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

        if (event.getPacket() instanceof EntitySpawnS2CPacket spawnPacket)
        {
            if (spawnPacket.getId() == 51)
            {
                for (Entity ent : mc.world.getEntities())
                {
                    if (ent == null) return;

                    if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ()) <= hitRange.getFValue())
                    {
                        int entity = crystal.getId();
                        mc.executeSync(() -> {
                            mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                            mc.world.removeBlockEntity(crystal.getBlockPos());
                        });
                    }
                }
            }
        }

        if (event.packet instanceof PlayerInteractBlockC2SPacket packet) {
            if (isOwn(packet.getBlockHitResult().getBlockPos().up()))
                ownCrystals.remove(packet.getBlockHitResult().getBlockPos().up());

            ownCrystals.put(packet.getBlockHitResult().getBlockPos().up(), System.currentTimeMillis());
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

    private boolean isValid(Entity entity)
    {
        return entity instanceof PlayerEntity && players.value() || EntityUtils.isMob(entity) && mobs.value() || EntityUtils.isAnimal(entity) && animals.value();
    }

    private void place(BlockPos pos, EndCrystalEntity entity)
    {
        Hand hand = null;

        Direction dir = getDirection(pos);

        BlockHitResult result = new BlockHitResult(pos.toCenterPos(), dir, pos, false);

        switch (hands.getMode()) {
            case "Main" -> hand = Hand.MAIN_HAND;
            case "Off" -> hand = Hand.OFF_HAND;
        }

        Hand finalHand = hand;
        switch (breaks.getMode()) {
            case "Vanilla" -> mc.interactionManager.interactBlock(mc.player, hand, result);
            case "Packet" ->
                    sendSeq(id -> new PlayerInteractBlockC2SPacket(finalHand, result, id));
        }

        if (swingOn.is("Both") && swingOn.is("Place")) {
            swingType(hand);
        }
    }

    private Direction getDirection(BlockPos blockPos)
    {

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if (mc.world.isInBuildLimit(blockPos))
        {
            return Direction.DOWN;
        }

        BlockHitResult result = mc.world.raycast(new RaycastContext(mc.player.getEyePos(), new Vec3d(x + 0.5, y + 0.5, z + 0.5), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

        if (result != null && result.getType() == HitResult.Type.BLOCK)
        {
            return result.getSide();
        }

        return Direction.UP;
    }
}
