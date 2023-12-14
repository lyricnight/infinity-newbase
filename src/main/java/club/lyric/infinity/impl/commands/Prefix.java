package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * prefix command
 */

public class Prefix extends Command {

    public Prefix() {
        super("prefix");
    }

    @Override
    public String theCommand()
    {
        return "prefix <char>";
    }

    @Override
    public void onCommand(String[] args) {
        String character = null;

        if (args.length > 1) {
            character = args[1];
        }

        if (character == null || args.length > 2) {
            state(CommandState.ERROR);
            return;
        }

        Infinity.COMMANDS.setPrefix(character);

        ChatUtils.sendMessagePrivate(Formatting.GREEN + "Prefix changed to " + Formatting.WHITE + character);

        state(CommandState.PERFORMED);
    }
}
