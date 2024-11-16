package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.impl.events.network.PacketEvent;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * @author vasler
 */

@SuppressWarnings({"unused"})
public class Criticals extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "NCP", "NCP", "NCPStrict", "Jump", "LowHop", "Grim");

    public Criticals() {
        super("Criticals", "Gives you a critical hit everytime you hit something.", Category.Combat);
    }

    @EventHandler(priority = Integer.MIN_VALUE + 2)
    public void onPacketSend(PacketEvent.Send event) {
        if ((event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && parseInteractType(packet) == PlayerInteractEntityC2SPacket.InteractType.ATTACK) && !(fetchEntityFromPacket(event.getPacket()) instanceof EndCrystalEntity) && mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            switch (mode.getMode()) {
                case "Grim" -> {
                    if (!mc.player.isOnGround())
                        send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y - 0.000001, z, false));
                }
                case "NCP" -> {
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.05, z, false));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.03, z, false));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
                }
                case "NCPStrict" -> {
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625d, z, false));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 1.1e-5d, z, false));
                }
                case "Jump" -> mc.player.jump();
                case "LowHop" -> {
                    mc.player.setVelocity(new Vec3d(0, 0.3425, 0));
                    mc.player.fallDistance = 0.1f;
                    mc.player.onGround = false;
                }
            }
        }
    }

    public static Entity fetchEntityFromPacket(PlayerInteractEntityC2SPacket packet) {

        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        packet.write(buffer);

        int entityId = buffer.readVarInt();
        return mc.world.getEntityById(entityId);

    }

    public static PlayerInteractEntityC2SPacket.InteractType parseInteractType(PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        packet.write(buffer);
        buffer.readVarInt();

        return buffer.readEnumConstant(PlayerInteractEntityC2SPacket.InteractType.class);

    }

    @Override
    public String moduleInformation() {
        return mode.getMode();
    }
}
