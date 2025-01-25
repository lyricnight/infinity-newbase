package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * gets the hashCode of a module (checks it has been loaded and can receive events) - for debugging
 */
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

        ModuleBase base = Managers.MODULES.getModuleByName(module);

        if (base == null)
        {
            state(CommandState.ERROR);
            return;
        }

        Managers.MESSAGES.sendMessage("Module ID:" + base.hashCode(), true);
        state(CommandState.PERFORMED);
    }

    @Override
    public String[] syntax(String string)
    {
        return new String[]{"<module>"};
    }
}
