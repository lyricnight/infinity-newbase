package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.manager.Managers;

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

        Managers.COMMANDS.setPrefix(character);

        //ChatUtils.sendMessage("Prefix changed to " + ChatFormatting.WHITE + character);

        state(CommandState.PERFORMED);
    }
}
