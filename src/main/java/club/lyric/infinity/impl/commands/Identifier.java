package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;

public final class Identifier extends Command {

    public Identifier()
    {
        super("identify");
    }

    @Override
    public String theCommand()
    {
        return "identify <module>";
    }

    @Override
    public void onCommand(String[] args)
    {
        if (args.length < 1) {
            state(CommandState.ERROR);
            return;
        }

        String module = args[1];

        if (Managers.MODULES.getModuleByName(module) == null)
        {
            state(CommandState.ERROR);
            return;
        }

        ChatUtils.sendMessagePrivate("Module ID:" + Managers.MODULES.getModuleByName(module).hashCode());
    }
}
