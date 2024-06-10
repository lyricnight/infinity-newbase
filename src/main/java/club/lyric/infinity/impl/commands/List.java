package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * for all commands
 */

public final class List extends Command {
    public List() {
        super("commands");
    }

    @Override
    public String theCommand()
    {
        return "commands";
    }

    @Override
    public void onCommand(String[] args)
    {

        if(args.length > 1)
        {
            state(CommandState.ERROR);
            return;
        }

        ChatUtils.sendMessagePrivate(Formatting.WHITE + String.format("Commands: (%s) ", Managers.COMMANDS.getCommandAmount()) + Managers.COMMANDS.getCommandsAsString());
        state(CommandState.PERFORMED);
    }

    @Override
    public String[] syntax(String string)
    {
        return new String[0];
    }
}
