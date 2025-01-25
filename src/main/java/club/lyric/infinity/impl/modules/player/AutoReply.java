package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.impl.events.mc.chat.ReceiveChatEvent;
import club.lyric.infinity.impl.events.mc.movement.PlayerMovementEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;
import java.util.stream.Stream;

/**
 * @author lyric
 */
//TODO debug
public final class AutoReply extends ModuleBase {
    public BooleanSetting armor = new BooleanSetting("Armor", false, this);
    public BooleanSetting coords = new BooleanSetting("Coords", true, this);
    public BooleanSetting afk = new BooleanSetting("AFK Responses", true, this);
    public BooleanSetting sendAnyway = new BooleanSetting("Send", false, this);
    public BooleanSetting debug = new BooleanSetting("d", false, this);
    public NumberSetting time = new NumberSetting("AFK Time", this, 30, 10, 100, 5);
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    private final DecimalFormat format = new DecimalFormat("0.##");

    public AutoReply() {
        super("AutoReply", "m", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        stopWatch.reset();
        if (!afk.value() && !coords.value() && !armor.value()) {
            Managers.MESSAGES.sendMessage(Formatting.RED + "You have not enabled either coordinate reply or afk mode, disabling...", false);
            disable();
        }
    }

    @EventHandler
    public void onChatMessageReceived(ReceiveChatEvent event) {
        Text message = event.getMessage();
        String string = message.getString();
        String ign = string.split(" ")[0];
        if (debug.value()) {
            Managers.MESSAGES.sendMessage("Got message:" + string, false);
        }
        if (sendAnyway.value())
        {
            send(MessageType.COORDINATE, "hi");
            send(MessageType.AFK, "hi");
            sendAnyway.setValue(false);
            return;
        }
        if (Stream.of("whispers:", "says:", "whispers to you:").anyMatch(string::contains) && !message.getString().contains(Formatting.WHITE.toString())) {
            if (debug.value()) {
                Managers.MESSAGES.sendMessage("Got checkandsend:" + string + ", " + ign, false);
            }
            checkAndSend(string, ign);
        }
    }

    @EventHandler
    public void onMove(PlayerMovementEvent ignored) {
        stopWatch.reset();
    }

    private void checkAndSend(String message, String ign) {
        switch (determineMessageType(message, ign)) {
            case COORDINATE -> send(MessageType.COORDINATE, ign);
            case AFK -> send(MessageType.AFK, ign);
        }
    }

    private MessageType determineMessageType(String message, String ign) {
        if (Stream.of("coords", "cords", "wya", "where u at", "where", "location").anyMatch(message::contains) && Managers.FRIENDS.isFriend(ign) && mc.player.getZ() < 10000 && mc.player.getX() < 10000 && coords.value()) {
            if (debug.value()) {
                Managers.MESSAGES.sendMessage("Got COORDINATE", false);
            }
            return MessageType.COORDINATE;
        }
        if (afk.value() && stopWatch.hasBeen(time.getIValue() * 1000L)) {
            if (debug.value()) {
                Managers.MESSAGES.sendMessage("Got AFK", false);
            }
            return MessageType.AFK;
        } else {
            if (debug.value()) {
                Managers.MESSAGES.sendMessage("Got NONE", false);
            }
            return MessageType.NONE;
        }
    }

    private void send(MessageType type, String ign) {
        if (debug.value()) {
            Managers.MESSAGES.sendMessage("Got SEND", false);
        }
        switch (type) {
            case COORDINATE -> mc.getNetworkHandler().sendChatCommand("/w " + ign + " [Infinity] Coordinates: " + format.format(mc.player.getX()) + ", " + format.format(mc.player.getZ()));
            case AFK -> mc.getNetworkHandler().sendChatCommand("/w " + ign + " [Infinity] I'm AFK.");
        }
    }

    private enum MessageType {
        COORDINATE,
        AFK,
        NONE
    }
}
