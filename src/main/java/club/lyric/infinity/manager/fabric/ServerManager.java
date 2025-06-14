package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.network.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.ArrayDeque;

/**
 * @author lyric
 * for handling TPS and ping to server.
 */
@SuppressWarnings("unused")
public final class ServerManager implements IMinecraft {

    /**
     * for our tps
     */
    private float tps;

    /**
     * queue of WorldTimeUpdate packets
     * this is NOT thread-safe so maybe can be replaced if I ever thread it?
     */
    private final ArrayDeque<Float> queue = new ArrayDeque<>(20);

    /**
     * for TPS timing.
     */
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    /**
     * our Latency ping.
     */
    @Getter
    @Setter
    private int ping = 0;

    /**
     * used in Latency
     */
    @Getter
    @Setter
    private long responseTime;

    /**
     * used in TPS
     */
    private long time;

    /**
     * gets our ping that the server tells us
     * @return - above
     */
    public int getServerPing()
    {
        if (Null.is()) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }

    /**
     * highest priority event for events.
     * @param event - packet event.
     * @see club.lyric.infinity.impl.modules.exploit.FastLatency
     */
    @EventHandler(priority = Integer.MAX_VALUE)
    public void onPacketReceive(PacketEvent.Receive event)
    {
        if(!(event.getPacket() instanceof ChatMessageS2CPacket))
        {
            stopWatch.reset();
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            if (time != 0L) {
                long timePerTick = System.currentTimeMillis() - time;

                if (queue.size() > 20) queue.poll();
                queue.add(20.0f * (1000.0f / (timePerTick)));

                float average = 0.0f;

                for (Float value : queue) average += MathUtils.clamp(value, 0f, 20f);

                tps = average / (float) queue.size();
            }
            time = System.currentTimeMillis();
        }
    }

    public float getOurTPS()
    {
        return MathUtils.round(tps);
    }

    public boolean isServerNotResponding() {
        return stopWatch.hasBeen(2500);
    }
}
