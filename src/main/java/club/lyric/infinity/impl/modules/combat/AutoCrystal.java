package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoCrystal extends ModuleBase {
    public NumberSetting hitRange = new NumberSetting("HitRange", this, 6.0f, 1.0f, 7.0f, 0.1f);
    public NumberSetting hitDelay = new NumberSetting("HitDelay", this, 15.0f, 0.0f, 600.0f, 0.1f);
    public ModeSetting hands = new ModeSetting("Hand", this, "Main", "Main", "Off");
    private StopWatch.Single breakTimer = new StopWatch.Single();

    public AutoCrystal() {
        super("AutoCrystal", "automatically crystal", Category.Combat);
    }

    @Override
    public void onEnable() {
        breakTimer.reset();
    }

    @Override
    public void onUpdate() {
        List<LivingEntity> targets = Streams.stream(mc.world.getEntities()).filter(e -> e instanceof PlayerEntity).filter(e -> e != mc.player).map(e -> (LivingEntity) e).toList();
        if (mc.player != null && mc.interactionManager != null) {
            if (mc.crosshairTarget instanceof EntityHitResult hit) {
                if (hit.getEntity() instanceof EndCrystalEntity && breakTimer.hasBeen(hitDelay.getValue())) {
                    if (mc.player.distanceTo(hit.getEntity()) >= hitRange.getValue() || mc.world.getOtherEntities(null, new Box(hit.getEntity().getPos(), hit.getEntity().getPos()).expand(7), targets::contains).isEmpty()) {
                        mc.interactionManager.attackEntity(mc.player, hit.getEntity());
                        switch (hands.getMode()) {
                            case "Main" -> mc.player.swingHand(Hand.MAIN_HAND);
                            case "Off" -> mc.player.swingHand(Hand.OFF_HAND);
                        }
                        breakTimer.reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPacketRecieve(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ExplosionS2CPacket explosion) {
            for (Entity ent : Lists.newArrayList(mc.world.getEntities())) {
                if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(explosion.getX(), explosion.getY(), explosion.getZ()) <= 6.0d) {
                    int entity = crystal.getId();
                    mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                }
            }
        }

        if (event.getPacket() instanceof PlaySoundS2CPacket sound) {
            if (sound.getCategory() == SoundCategory.BLOCKS && sound.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity ent : Lists.newArrayList(mc.world.getEntities())) {
                    if (ent instanceof EndCrystalEntity crystal && crystal.squaredDistanceTo(sound.getX(), sound.getY(), sound.getZ()) < 36.0d) {
                        int entity = crystal.getId();
                        mc.world.removeEntity(entity, Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }
}
