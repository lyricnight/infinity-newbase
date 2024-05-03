package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Formatting;

import java.util.HashMap;

/**
 * @author vasler
 * NOTIFIY
 */
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

    public Notifications()
    {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }
}
