package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.keyboard.KeyUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

public class Bind extends Command {

    protected ModuleBase moduleBase;
    protected boolean keyPressEnable;
    //protected StopWatch stopWatch = new StopWatch.Single();

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

        ChatUtils.sendOverwriteMessageColored("Press any " + Formatting.WHITE + "key" + Formatting.RESET + " on your keyboard.", 8948);

        // stopwatch doesnt work here so
        new Thread(() -> {
            try {
                Thread.sleep(500);
                keyPressEnable = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public String[] syntax(String string)
    {
        return new String[]{"<module>"};
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {

        if (!keyPressEnable) return;

        keyPressEnable = false;

        moduleBase.setBind(event.getKey());
        ChatUtils.sendOverwriteMessageColored("The bind for " + moduleBase.getName() + " has changed to " + Formatting.WHITE + KeyUtils.getKeyName(event.getKey()) + Formatting.RESET + ".", 8948);
    }

}
