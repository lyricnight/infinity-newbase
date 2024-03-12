package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Velocity extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "JumpReset");
    protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    protected static final Random random = new Random();

    public Velocity() {
        super("Velocity", "Velocity", Category.Movement);
    }

    @EventHandler
    public void onEntityVelocity(PacketEvent.Receive event) {
        if (mode.getMode().equals("JumpReset")) {
            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket && mc.player.hurtTime > 0 && mc.player.isOnGround()) {
                if (random.nextDouble() < 0.4) {
                    int value = random.nextInt(5) + 1;
                    mc.player.jump();
                    executor.schedule(() -> mc.player.jump(), value, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
