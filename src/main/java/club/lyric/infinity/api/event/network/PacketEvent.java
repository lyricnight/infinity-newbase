package club.lyric.infinity.api.event.network;

import me.bush.eventbus.event.Event;
import net.minecraft.network.packet.Packet;

/**
 * @author lyric
 * packet events.
 */

public class PacketEvent extends Event {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }
    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    @Override
    protected boolean isCancellable() {
        return true;
    }
}
