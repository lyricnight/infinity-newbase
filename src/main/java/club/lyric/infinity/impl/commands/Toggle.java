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

        ChatUtils.sendMessagePrivate("Module input :" + module);

        for (ModuleBase modules : Managers.MODULES.getModules()) {
            ChatUtils.sendMessagePrivate("Module iterated to: " + modules.getName());
            if (modules.getName().equalsIgnoreCase(module)) {
                ChatUtils.sendMessagePrivate("Module equivalent to: " + modules.getName() + " input is:" + module);
                ChatUtils.sendMessagePrivate(Formatting.BOLD + modules.getName() + Formatting.RESET + " " + "has been toggled.");
                modules.toggle();
                state(CommandState.PERFORMED);
                return;
            }
        }
        state(CommandState.ERROR);
    }


}

