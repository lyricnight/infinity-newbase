package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.impl.events.mc.chat.ReceiveChatEvent;
import club.lyric.infinity.impl.events.mc.movement.MotionEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.stream.Stream;

/**
 * @author lyric
 */
public class AutoReply extends ModuleBase {
    public BooleanSetting armor = new BooleanSetting("Armor", false, this);
    public BooleanSetting coords = new BooleanSetting("Coords", true, this);
    public BooleanSetting afk = new BooleanSetting("AFK Responses",true, this);
    public BooleanSetting debug = new BooleanSetting("d", false, this);
    public NumberSetting time = new NumberSetting("AFK Time", this, 30, 10, 100, 5);
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    public AutoReply()
    {
        super("AutoReply", "m", Category.Player);
    }

    @Override
    public void onEnable()
    {
        stopWatch.reset();
        if (!afk.value() && !coords.value() && !armor.value())
        {
            ChatUtils.sendMessagePrivate(Formatting.RED + "You have not enabled either coordinate reply or afk mode, disabling...");
            disable();
        }
    }

    @EventHandler
    public void onChatMessageReceived(ReceiveChatEvent event)
    {
        Text message = event.getMessage();
        String string = message.getString();
        String ign = string.split(" ")[0];
        if (debug.value())
        {
            ChatUtils.sendMessagePrivate("Got message:" + string);
        }
        if (Stream.of("whispers:", "says:", "whispers to you:").anyMatch(string::contains) && !message.getString().contains(Formatting.WHITE.toString()))
        {
            if (debug.value())
            {
                ChatUtils.sendMessagePrivate("Got checkandsend:" + string + ", " + ign);
            }
            checkAndSend(string, ign);
        }
    }

    @EventHandler
    public void onMove(MotionEvent ignored)
    {
        stopWatch.reset();
    }

    private void checkAndSend(String message, String ign)
    {
        switch (determineMessageType(message, ign))
        {
            case COORDINATE -> send(MessageType.COORDINATE, ign);
            case AFK -> send(MessageType.AFK, ign);
        }
    }

    private MessageType determineMessageType(String message, String ign)
    {
        if (Stream.of("coords", "cords", "wya", "where u at", "where", "location").anyMatch(message::contains) && Managers.FRIENDS.isFriend(ign) && mc.player.getZ() < 10000 && mc.player.getX() < 10000 && coords.value())
        {
            if (debug.value())
            {
                ChatUtils.sendMessagePrivate("Got COORDINATE");
            }
            return MessageType.COORDINATE;
        }
        if (afk.value() && stopWatch.hasBeen(time.getIValue() * 1000L))
        {
            if (debug.value())
            {
                ChatUtils.sendMessagePrivate("Got AFK");
            }
            return MessageType.AFK;
        }
        else
        {
            if (debug.value())
            {
                ChatUtils.sendMessagePrivate("Got NONE");
            }
            return MessageType.NONE;
        }
    }

    private void send(MessageType type, String ign)
    {
        if (debug.value())
        {
            ChatUtils.sendMessagePrivate("Got SEND");
        }
        switch (type)
        {
            case COORDINATE -> mc.player.sendMessage(Text.of("/w " + ign + " [Infinity] Coordinates: " + mc.player.getX() + ", " + mc.player.getZ() + ", Dimension: " + mc.world.getDimension().toString()));
            case AFK -> mc.player.sendMessage(Text.of("/w " + ign  + " [Infinity] I'm AFK. Wait a bit."));
        }
    }

    private enum MessageType
    {
        COORDINATE,
        AFK,
        NONE
    }
}
