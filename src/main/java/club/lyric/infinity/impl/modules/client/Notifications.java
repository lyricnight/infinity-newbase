package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.chat.ID;
import me.lyric.eventbus.annotation.EventListener;
import me.lyric.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;

/**
 * @author vasler
 * NOTIFIY
 */
public class Notifications extends ModuleBase
{

    public BooleanSetting totemPops = new BooleanSetting(
            "TotemPops",
            false,
            "Notifies when an enemy gets totem popped."
    );
    public BooleanSetting enable = new BooleanSetting(
            "Enabled",
            false,
            "Notifies when you enable a module."
    );
    public BooleanSetting disable = new BooleanSetting(
            "Disabled",
            false,
            "Notifies when you disable a module."
    );

    private final HashMap<String, Integer> totemPop = new HashMap<>();

    public Notifications()
    {
        super("Notifications", "Notifies in chat for stuff.", Category.CLIENT);
        instantiate(this, totemPops, enable, disable);
    }

    @Override
    public void onEnable()
    {
        totemPop.clear();
    }

    @EventListener(priority = ListenerPriority.LOW)
    private void onReceivePacket(PacketEvent.Receive event)
    {
        if (totemPops.getValue())
        {
            if (event.getPacket() instanceof EntityStatusS2CPacket packet)
            {
                Entity entity = packet.getEntity(mc.world);
                if (packet.getStatus() == 35)
                {
                    int pops = totemPop.get(entity.getEntityName()) == null ? 1 : totemPop.get(entity.getEntityName()) + 1;
                    totemPop.put(entity.getEntityName(), pops);
                    ChatUtils.sendOverwriteMessage(entity.getEntityName() + " popped " + totemPop.get(entity.getEntityName()) + " time(s).", ID.TOTEM_POPS);
                }
            }
        }
    }
}
