package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* TODO: make calculated breaking smarter by: calculating enemy and own damage and finding the best possible crystal to break;
* make placing modes like breaking: do the same shit i just said; ADD PLACING, add id predict*/
public class AutoCrystal extends ModuleBase {
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
    public void onEnable() {
        breakTimer.reset();
        ownCrystals.clear();
    }

    @Override
    public void onUpdate() {

        assert mc.world != null;
        List<LivingEntity> targets = Streams.stream(
                mc.world.getEntities())
                .filter(e -> e instanceof PlayerEntity)
                .filter(e -> e != mc.player)
                .map(e -> (LivingEntity) e)
                .toList();
        assert mc.player != null;

        List<EndCrystalEntity> near = Streams.stream
                (mc.world.getEntities())
                .filter(e -> e instanceof EndCrystalEntity)
                .map(e -> (EndCrystalEntity) e)
                .sorted(Comparator.comparing(mc.player::distanceTo))
                .toList();

        // breaking
        if (mc.player != null && mc.interactionManager != null) {
            for (EndCrystalEntity crystal : near) {

                if (crystal == null || crystal.age < tickExisted.getValue()) {
                    return;
                }

                if (breakTimer.hasBeen(hitDelay.getLValue())) {

                    if (Objects.equals(breaking.getMode(), "Calculated") && !isOwn(crystal.getPos())) {
                        return;
                    }

                    if (mc.player.distanceTo(crystal) >= hitRange.getValue() ||
                            mc.world.getOtherEntities(null,
                                    new Box(crystal.getPos(),
                                            crystal.getPos()).expand(7),
                                    targets::contains).isEmpty()
                    ) {

                        if (inhibit.value()) {
                            StopWatch.Single timer = hitCrystals.get(crystal.getId());
                            if (timer != null) {
                                if (!timer.hasBeen((timeout.getLValue()))) {
                                    continue;
                                }
                                hitCrystals.remove(crystal.getId());
                            }
                        }

                        service.submit(() -> {
                            hitCrystals.put(crystal.getId(), new StopWatch.Single());

                            switch (breaks.getMode()) {
                                case "Vanilla" -> mc.interactionManager.attackEntity(mc.player, crystal);
                                case "Packet" ->
                                        send(PlayerInteractEntityC2SPacket.attack(mc.player, mc.player.isSneaking()));
                            }

                            if (Objects.equals(swingOn.getMode(), "Both") && Objects.equals(swingOn.getMode(), "Break")) {
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
            if (event.getPacket() instanceof ExplosionS2CPacket explosion) {
                assert mc.world != null;
                for (Entity ent : mc.world.getEntities()) {
                    if (ent == null) {
                        return;
                    }
                    if (ent instanceof EndCrystalEntity crystal &&

                            crystal.squaredDistanceTo(
                                    explosion.getX(),
                                    explosion.getY(),
                                    explosion.getZ()
                            ) <= 6.0d)
                    {
                        int entity = crystal.getId();
                        mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                        mc.world.removeBlockEntity(crystal.getBlockPos());
                    }
                }
            }
        }

        if (sound.value()) {
            if (event.getPacket() instanceof PlaySoundS2CPacket sound) {
                if (sound.getCategory() == SoundCategory.BLOCKS && sound.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    assert mc.world != null;
                    for (Entity ent : mc.world.getEntities()) {
                        if (ent == null) {
                            return;
                        }
                        if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(sound.getX(), sound.getY(), sound.getZ()) < 36.0d) {
                            int entity = crystal.getId();
                            mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                            mc.world.removeBlockEntity(crystal.getBlockPos());
                        }
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
}
