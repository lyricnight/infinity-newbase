package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.chat.ChatUtils;

public class Toggle extends Command {
    public Toggle() {
        super("toggle");
    }

    @Override
    public void onCommand(String[] args)
    {
        if (args.length < 1 || args[0] == null) {
            notFound();
            return;
        }
        ModuleBase mod = Infinity.MODULES.getModuleByName(args[0]);
        if (mod == null) {
            notFound();
            return;
        }
        mod.setEnabled(true);
    }

    private void notFound() {
        ChatUtils.sendMessagePrivate("Module is not found.");
    }
}