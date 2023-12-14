package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.mc.ChatEvent;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


/**
 * @author lyric
 * for events
 */

public class EventManager implements IMinecraft {


    /**
     * for commands.
     * @param event - the chat event
     */
    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith(Managers.COMMANDS.getPrefix())) {
            event.cancel();

            String[] arguments = message.replaceFirst(Managers.COMMANDS.getPrefix(), "").split(" ");

            mc.inGameHud.getChatHud().addMessage(Text.literal(message));

            boolean isCommand = false;

            for (Command commands : Managers.COMMANDS.getCommands()) {
                if (commands.getCommand().equals(arguments[0])) {
                    commands.onCommand(arguments);

                    isCommand = true;

                    break;
                }
            }
            if (!isCommand) {
                ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown command. Try " + Managers.COMMANDS.getPrefix() + "commands for a list of available commands.");
            }
        }
    }

    @EventHandler
    public void onPacket(PacketEvent.Send event)
    {
        Infinity.LOGGER.info("This works??");
        ChatUtils.sendMessagePrivate("THIS WORKS??");
    }



}
