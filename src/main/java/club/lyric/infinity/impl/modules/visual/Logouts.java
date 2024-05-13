package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class Logouts extends ModuleBase {

    protected Map<PlayerEntity, UUID> logouts = new ConcurrentHashMap<>();

    public Logouts()
    {
        super("Logouts", "Renders the spots where players have logged out.", Category.Visual);
    }

    @Override
    public void onDisable()
    {
        logouts.clear();
    }

    private void remove(PlayerEntity entity)
    {

        for (PlayerEntity e : logouts.keySet())
        {

            if (e.getName().getString().equals(entity.getName().getString()))
            {

                logouts.remove(e);
                break;

            }

        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack)
    {

        logouts.forEach((pos, player) -> {

        });
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event)
    {
        if (event.getPacket() instanceof PlayerRemoveS2CPacket)
        {

            for (UUID uuid : ((PlayerRemoveS2CPacket) event.getPacket()).profileIds())
            {

                PlayerEntity player = mc.world.getPlayerByUuid(uuid);

                if (player != null && player != mc.player)
                {
                    logouts.put(player, uuid);
                }

            }
        }
    }
}
