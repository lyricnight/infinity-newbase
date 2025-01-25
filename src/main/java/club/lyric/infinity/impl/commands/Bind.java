package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.keyboard.KeyUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author vasler
 */
public final class Bind extends Command {

    private ModuleBase moduleBase;

    private boolean keyPressEnable;

    public Bind()
    {
        super("bind");
        EventBus.getInstance().register(this);
    }

    @Override
    public String theCommand()
    {
        return "bind <module>";
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 2 || args.length < 1)
        {
            state(CommandState.ERROR);
            return;
        }

        moduleBase = Managers.MODULES.getModuleByName(args[1]);

        if (moduleBase == null)
        {
            state(CommandState.ERROR);
            return;
        }

        Managers.MESSAGES.sendOverwriteMessage("Press any " + Formatting.WHITE + "key" + Formatting.RESET + " on your keyboard.", 8948, true);

        // stopwatch doesnt work here so
        //yes it does vasler?
        //StopWatch.Multi
        new Thread(() -> {
            try {
                Thread.sleep(500);
                keyPressEnable = true;
            } catch (InterruptedException e) {
                Infinity.LOGGER.atError();
            }
        }).start();

    }

    @Override
    public String[] syntax(String string)
    {
        return new String[]{"<module>"};
    }

    @EventHandler(priority = -3)
    public void onKeyPress(KeyPressEvent event) {

        if (!keyPressEnable) return;

        keyPressEnable = false;

        moduleBase.setBind(event.getKey());
        Managers.MESSAGES.sendOverwriteMessage("The bind for " + moduleBase.getName() + " has changed to " + Formatting.WHITE + KeyUtils.getKeyName(event.getKey()) + Formatting.RESET + ".", 8948, true);
    }
}
