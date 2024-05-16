package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.chat.irc.IRCChat;
import club.lyric.infinity.impl.events.irc.IRCEvent;
import club.lyric.infinity.impl.events.mc.chat.ChatSentEvent;
import net.minecraft.util.Formatting;

public class IRC extends ModuleBase {

    public IRC() {
        super("IRC", "Allows you to chat to other Infinity users.", Category.Client);
    }

    @Override
    public void onEnable()
    {
        IRCChat.join();
        ChatUtils.sendMessagePrivate(Formatting.GRAY + "You can chat in the IRC by using the % prefix.");
    }

    @Override
    public void onDisable()
    {
        IRCChat.disconnect();
    }

    @EventHandler(priority = 800)
    public void onChatSent(ChatSentEvent event)
    {
        if (event.getMessage().startsWith("% "))
        {
            event.setCancelled(true);
            String sub = event.getMessage().substring("% ".length());
            if (sub.length() < 150) {
                IRCChat.chat(sub);
            } else {
                ChatUtils.sendMessagePrivateColored(Formatting.GRAY + "Messages cannot be above 150 characters.");
            }
        }
    }

    @EventHandler
    public void onReceived(IRCEvent event)
    {
        String text = event.getText().length() < 150 ? event.getText() : Formatting.RED + "Messages cannot be above 150 characters.";
        if (mc.player != null)
        {
            ChatUtils.sendMessagePrivateColored(event.getNick() + ": " + Formatting.GRAY + text);
        }
    }
}
