package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.math.Null;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

/**
 * @author valser
 * im deleting this - lyric
 */
@Deprecated
public class NoRotate extends ModuleBase {
    public NoRotate() {
        super("NoRotate", "hhh", Category.Player);
    }

    @EventHandler
    public void onPacketSend(PacketEvent.Receive event) {
        if (Null.is()) return;

        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            event.setCancelled(true);
        }
    }
}
