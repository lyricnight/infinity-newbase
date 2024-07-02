package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.asm.accessors.IEntityVelocityUpdateS2CPacket;
import club.lyric.infinity.asm.accessors.IExplosionS2CPacket;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author vasler
 * fix this - lyric
 */
@SuppressWarnings({"unused"})
public class Velocity extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "JumpReset", "Grim");
    public NumberSetting horizontal = new NumberSetting("Horizontal", this, 0.0f, -100.0f, 100.0f, 1.0f);
    public NumberSetting vertical = new NumberSetting("Vertical", this, 0.0f, -100.0f, 100.0f, 1.0f);

    protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    protected static final Random random = new Random();
    protected StopWatch.Single stopWatch = new StopWatch.Single();

    public Velocity() {
        super("Velocity", "Tries to remove velocity", Category.Movement);
    }

    @EventHandler
    public void onEntityVelocity(PacketEvent.Receive event) {

        if (nullCheck()) return;

        if (mode.is("JumpReset")) {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket && mc.player.hurtTime > 0 && mc.player.isOnGround() && mc.player.isAlive()) {

                if (random.nextDouble() < 0.4) {

                    int value = random.nextInt(5) + 1;

                    executor.schedule(() -> mc.player.jump(), value, TimeUnit.MILLISECONDS);

                }

            }
        } else if (mode.is("Grim")) {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {

                if (!stopWatch.hasBeen(100)) return;

                if (event.getPacket() instanceof PlayerPositionLookS2CPacket) return;

                event.setCancelled(true);

            }
        } else if (mode.is("Normal")) {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {

                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityX((int) (((double) ((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityX() / 100 - mc.player.getVelocity().x) * horizontal.getIValue() * 100 + mc.player.getVelocity().x * 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityY((int) (((double) ((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityY() / 100 - mc.player.getVelocity().y) * vertical.getIValue() * 100 + mc.player.getVelocity().y * 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityZ((int) (((double) ((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityZ() / 100 - mc.player.getVelocity().z) * horizontal.getIValue() * 100 + mc.player.getVelocity().z * 100));

            }

            if (event.getPacket() instanceof ExplosionS2CPacket) {

                ((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityX((int) (((double) ((ExplosionS2CPacket) event.getPacket()).getPlayerVelocityX() / 100 - mc.player.getVelocity().x) * horizontal.getIValue() * 100 + mc.player.getVelocity().x * 100));
                ((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityY((int) (((double) ((ExplosionS2CPacket) event.getPacket()).getPlayerVelocityY() / 100 - mc.player.getVelocity().y) * vertical.getIValue() * 100 + mc.player.getVelocity().y * 100));
                ((IExplosionS2CPacket) event.getPacket()).setPlayerVelocityZ((int) (((double) ((ExplosionS2CPacket) event.getPacket()).getPlayerVelocityZ() / 100 - mc.player.getVelocity().z) * horizontal.getIValue() * 100 + mc.player.getVelocity().z * 100));

            }
        }

    }

    @Override
    public String moduleInformation() {
        if (mode.is("Grim") || mode.is("JumpReset")) {
            return mode.getMode();
        }
        return "H" + horizontal.getFValue() + "%" + Formatting.GRAY + "," + Formatting.WHITE + "V" + vertical.getFValue() + "%";
    }
}
