package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;

/**
 * @author vasler
 * NOTIFIY
 */
public final class Notifications extends ModuleBase
{

    public BooleanSetting totemPops =
            new BooleanSetting(
            "TotemPops",
            false,
            this
    );
    public BooleanSetting enable =
            new BooleanSetting(
            "Enabled",
            true,
            this
    );
    public BooleanSetting disable =
            new BooleanSetting(
            "Disabled",
            true,
            this
    );

    private final HashMap<String, Integer> totemPop = new HashMap<>();
    private final HashMap<String, Integer> id = new HashMap<>();

    public Notifications()
    {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }


    @Override
    public void onEnable()
    {
        totemPop.clear();
    }

    @Override
    public void onUpdate() {
        if(nullCheck()) return;
        mc.world.getPlayers().forEach(player -> {
            if(player.getHealth() <= 0) {
                if (totemPop.containsKey(player.getEntityName())) {
                    ChatUtils.sendOverwriteMessage(player.getEntityName() + " died after popping " + totemPop.get(player.getEntityName()) + " time(s).", player.getId());
                    totemPop.remove(player.getEntityName(), totemPop.get(player.getEntityName()));
                }
            }
        });
    }

    @EventHandler
    public void onReceivePacket(PacketEvent.Receive event)
    {
        if (nullCheck()) return;
        String uuidString = mc.player.getUuid().toString();
        String truncated = uuidString.substring(0, 4);
        id.put(truncated, Integer.valueOf(uuidString));

        if (totemPops.value())
        {
            if (event.getPacket() instanceof EntityStatusS2CPacket packet)
            {
                Entity entity = packet.getEntity(mc.world);
                if (packet.getStatus() == 35)
                {
                    int pops = totemPop.get(entity.getEntityName()) == null ? 1 : totemPop.get(entity.getEntityName()) + 1;
                    totemPop.put(entity.getEntityName(), pops);
                    ChatUtils.sendOverwriteMessage(entity.getEntityName() + " popped " + totemPop.get(entity.getEntityName()) + " time(s).", entity.getId());
                }
            }
        }
    }

}
