package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;

/**
 * @author vasler
 * NOTIFIY
 */
@SuppressWarnings("ConstantConditions")
public class Notifications extends PersistentModuleBase
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

    public BooleanSetting visualRange =
            new BooleanSetting(
            "VisualRange",
            false,
            this
    );

    private final HashMap<String, Integer> totemPop = new HashMap<>();

    public Notifications()
    {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }

    @Override
    public void onUpdate() {
        if(nullCheck()) return;
        mc.world.getPlayers().forEach(player -> {
            if(player.getHealth() <= 0) {
                if (totemPop.containsKey(player.getName().toString())) {
                    ChatUtils.sendOverwriteMessage(player.getName() + " died after popping " + totemPop.get(player.getName().toString()) + " time(s).", player.getId());
                    totemPop.remove(player.getName().toString(), totemPop.get(player.getName().toString()));
                }
            }
        });
    }

    @EventHandler
    public void onReceivePacket(PacketEvent.Receive event)
    {
        if (nullCheck()) return;

        if (totemPops.value())
        {
            if (event.getPacket() instanceof EntityStatusS2CPacket packet)
            {
                Entity entity = packet.getEntity(mc.world);
                if (packet.getStatus() == 35)
                {
                    int pops = totemPop.get(entity.getName().toString()) == null ? 1 : totemPop.get(entity.getName().toString()) + 1;
                    totemPop.put(String.valueOf(entity.getName()), pops);
                    ChatUtils.sendOverwriteMessage(entity.getName() + " popped " + totemPop.get(entity.getName().toString()) + " time(s).", entity.getId());
                }
            }
        }
    }
    



}
