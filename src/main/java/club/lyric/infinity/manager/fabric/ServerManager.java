package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.ArrayDeque;

/**
 * @author lyric
 * for handling TPS and ping to server.
 */
@SuppressWarnings("unused")
public class ServerManager implements IMinecraft {

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
    private int ping = 0;

    /**
     * used in Latency
     */
    public long responseTime;

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
        if (mc.getNetworkHandler() == null || mc.player == null) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }

    @EventHandler(priority = Integer.MAX_VALUE)
    public void onPacketReceive(PacketEvent.Receive event)
    {
        //We don't actually need to check if Latency is enabled, because time is the most important thing here, and receiving the packet we check for is impossible normally.
        if(event.getPacket() instanceof CommandSuggestionsS2CPacket commandSuggestionsS2CPacket && commandSuggestionsS2CPacket.getCompletionId() == 1337)
        {
            ping = (int) MathUtils.clamp(System.currentTimeMillis() - responseTime, 0, 2000);
        }

        if(!(event.getPacket() instanceof ChatMessageS2CPacket))
        {
            stopWatch.reset();
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            if (time != 0L) {
                long timePerTick = System.currentTimeMillis() - time;

                if (queue.size() > 20) queue.poll();

                queue.add(20.0f * (1000.0f / (float) (timePerTick)));

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

    /**
     * this gets fastLatency ping
     * you MUST CHECK if this returns 0 (means fastlatency is off)
     * @return ping
     */
    public int getFastLatencyPing()
    {
        return ping;
    }
}
