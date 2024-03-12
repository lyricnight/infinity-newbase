package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 */
public class Bind extends Command {
    public Bind()
    {
        super("bind");
    }

    @Override
    public String theCommand()
    {
        return "bind <module> <key>";
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 3 || args.length < 1)
        {
            state(CommandState.ERROR);
            return;
        }

        ModuleBase module = Managers.MODULES.getModuleByName(args[1]);

        if (module == null)
        {
            state(CommandState.ERROR);
            return;
        }

        module.setBind(Integer.parseInt(args[2]));
    }
}
