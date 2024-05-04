package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.asm.accessors.IEntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;

import java.text.Normalizer;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        if (mode.is("JumpReset"))
        {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket && mc.player.hurtTime > 0 && mc.player.isOnGround() && mc.player.isAlive())
            {

                if (random.nextDouble() < 0.4) {

                    int value = random.nextInt(5) + 1;

                    executor.schedule(() -> mc.player.jump(), value, TimeUnit.MILLISECONDS);

                }

            }
        }
        else if (mode.is("Grim"))
        {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {

                if (!stopWatch.hasBeen(100)) return;

                if (event.getPacket() instanceof PlayerPositionLookS2CPacket) return;

                event.setCancelled(true);

            }
        }
        else if (mode.is("Normal"))
        {

            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket)
            {

                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityX(((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityX() * (horizontal.getIValue() / 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityY(((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityY() * (vertical.getIValue() / 100));
                ((IEntityVelocityUpdateS2CPacket) event.getPacket()).setVelocityZ(((EntityVelocityUpdateS2CPacket) event.getPacket()).getVelocityZ() * (horizontal.getIValue() / 100));

            }
        }

    }

    @Override
    public void onTickPre() {
        if (mode.is("Grim"))
        {

            float yaw = mc.player.getYaw();
            float pitch = mc.player.getPitch();

            send(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), yaw, pitch, mc.player.isOnGround()));
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.getBlockPos(), mc.player.getHorizontalFacing().getOpposite()));

        }
    }

    @Override
    public String moduleInformation()
    {
        if (mode.is("Grim") || mode.is("JumpReset"))
        {
            return mode.getMode();
        }
        return "H" + horizontal.getFValue() + "%" + Formatting.GRAY + "|" + Formatting.WHITE + "V" + vertical.getFValue() + "%";
    }
}
