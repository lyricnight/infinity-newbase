package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * for all commands
 */

public class List extends Command {
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
        ChatUtils.sendMessagePrivate(Formatting.GREEN + Managers.COMMANDS.getCommandsAsString().toString());
        state(CommandState.PERFORMED);
    }
}
