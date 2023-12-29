package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * toggling....
 */

public class Toggle extends Command {

    public Toggle() {
        super("toggle");
    }

    @Override
    public String theCommand()
    {
        return "toggle <module>";
    }

    @Override
    public void onCommand(String[] args)
    {
        String module = null;

        if (args.length > 1) {
            module = args[1];
        }

        if (module == null || args.length > 2) {
            state(CommandState.ERROR);
            return;
        }

        for (ModuleBase modules : Managers.MODULES.getModules()) {
            if (modules.getName().equalsIgnoreCase(module)) {
                modules.toggle();
                state(CommandState.PERFORMED);
                return;
            }
        }
        state(CommandState.ERROR);
    }
}

