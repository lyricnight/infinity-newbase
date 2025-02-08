package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.chat.IRCChat;
import club.lyric.infinity.impl.events.irc.IRCEvent;
import club.lyric.infinity.impl.events.mc.chat.ChatSentEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author vasler and finz0
 */
public final class IRC extends ModuleBase {

    public IRC() {
        super("IRC", "Allows you to chat to other Infinity users.", Category.MISC);
    }

    @Override
    public void onEnable() {
        IRCChat.join();
        Managers.MESSAGES.sendMessage(Formatting.GRAY + "You can chat in the IRC by using the % prefix.", false);
    }

    @Override
    public void onDisable() {
        IRCChat.disconnect();
    }

    @EventHandler(priority = 800)
    public void onChatSent(ChatSentEvent event) {
        if (event.getMessage().startsWith("% ")) {
            event.setCancelled(true);
            String sub = event.getMessage().substring("% ".length());
            if (sub.length() < 150) {
                IRCChat.chat(sub);
            } else {
                Managers.MESSAGES.sendMessage(Formatting.GRAY + "Messages cannot be above 150 characters.", true);
            }
        }
    }

    @EventHandler
    public void onReceived(IRCEvent event) {
        String text = event.getText().length() < 150 ? event.getText() : Formatting.RED + "Messages cannot be above 150 characters.";
        if (mc.player != null) {
            Managers.MESSAGES.sendMessage(event.getNick() + ": " + Formatting.GRAY + text, true);
        }
    }

    @Override
    public String moduleInformation() {
        if (IRCChat.connected) {
            return Formatting.GREEN + "connected";
        }
        return Formatting.RED + "disconnected";
    }
}
