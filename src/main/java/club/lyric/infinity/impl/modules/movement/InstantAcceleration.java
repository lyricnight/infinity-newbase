package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.movement.EntityMovementEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @author vasler
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class InstantAcceleration extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Strict", "Strict", "Normal", "Grim");

    public InstantAcceleration()
    {
        super("InstantAcceleration", "Instantly accelerates to your maximum speed.", Category.Movement);
    }

    @EventHandler
    public void onMovement(EntityMovementEvent event)
    {
        if (mode.is("Strict"))
        {

            if (!mc.player.isOnGround()) return;

            MovementUtil.createSpeed(MovementUtil.getSpeed(true));

        }

        if (mode.is("Normal"))
        {

            MovementUtil.createSpeed(MovementUtil.getSpeed(true));

        }

        // might work? idk im just testing rando shit
        if (mode.is("Grim"))
        {

            float yaw = mc.player.getYaw();
            float pitch = mc.player.getPitch();

            double[] directionSpeed = MovementUtil.directionSpeed(MovementUtil.getSpeed(true));

            send(new PlayerMoveC2SPacket.Full(directionSpeed[0], mc.player.getY(), directionSpeed[1], yaw, pitch, mc.player.isOnGround()));
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.getBlockPos(), mc.player.getHorizontalFacing().getOpposite()));

        }
    }
}
