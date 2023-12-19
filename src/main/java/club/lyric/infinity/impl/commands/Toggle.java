package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

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

        boolean isModule = false;

        for (ModuleBase modules : Managers.MODULES.getModules()) {
            if (modules.getName().equalsIgnoreCase(module)) {
                if (Managers.MODULES.getModuleByString(module).isOn())
                {
                    Managers.MODULES.getModuleByString(module).disable();
                }
                else
                {
                    Managers.MODULES.getModuleByString(module).enable();
                }
                isModule = true;
                break;
            }
        }
        if (!isModule) {
            ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown module.");
        } else {
            ChatUtils.sendMessagePrivate(Formatting.BOLD + Managers.MODULES.getModuleByString(module).getName() + Formatting.RESET + " " + "has been toggled.");
        }

        state(CommandState.PERFORMED);
    }


}

