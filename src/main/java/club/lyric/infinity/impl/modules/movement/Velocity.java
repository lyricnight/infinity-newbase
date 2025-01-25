package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.asm.accessors.IEntityVelocityUpdateS2CPacket;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Formatting;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author vasler
 * TODO fix velocity
 */
@SuppressWarnings({"unused"})
public final class Velocity extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "JumpReset");
    public NumberSetting horizontal = new NumberSetting("Horizontal", this, 0.0f, -100.0f, 100.0f, 1.0f);
    public NumberSetting vertical = new NumberSetting("Vertical", this, 0.0f, -100.0f, 100.0f, 1.0f);
    public BooleanSetting push = new BooleanSetting("Push", true, this);

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Random random = new Random();
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    public Velocity() {
        super("Velocity", "Tries to remove velocity", Category.MOVEMENT);
    }

    @EventHandler
    public void onEntityVelocity(PacketEvent.Receive event) {

        if (Null.is()) return;

        if (mode.is("JumpReset")) {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket && mc.player.hurtTime > 0 && mc.player.isOnGround() && mc.player.isAlive()) {

                if (random.nextDouble() < 0.4) {

                    int value = random.nextInt(5) + 1;

                    executor.schedule(() -> mc.player.jump(), value, TimeUnit.MILLISECONDS);

                }

            }
        }
        else if (mode.is("Normal")) {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet) {

                if (horizontal.getFValue() == 0.0 && vertical.getFValue() == 0.0)
                {
                    event.setCancelled(true);
                }

                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityX((int) (mc.player.getX() * horizontal.getIValue() / 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityY((int) (mc.player.getY() * vertical.getIValue() / 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityZ((int) (mc.player.getZ() * horizontal.getIValue() / 100));

            }

            if (event.getPacket() instanceof ExplosionS2CPacket packet) {

                if (horizontal.getFValue() == 0.0 && vertical.getFValue() == 0.0)
                {
                    event.setCancelled(true);
                }
                //((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityX((int) (mc.player.getX() * horizontal.getIValue() / 100));
                //((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityY((int) (mc.player.getY() * vertical.getIValue() / 100));
                //((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityZ((int) (mc.player.getZ() * horizontal.getIValue() / 100));
            }
        }

    }

    @Override
    public String moduleInformation() {
        if (mode.is("JumpReset")) {
            return mode.getMode();
        }
        return "H" + horizontal.getFValue() + "%" + Formatting.GRAY + "," + Formatting.WHITE + "V" + vertical.getFValue() + "%";
    }
}
