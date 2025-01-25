package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.manager.Managers;

/**
 * @author vasler
 */
public final class Watermark extends Command
{
    public Watermark()
    {
        super("watermark");
    }

    @Override
    public String theCommand()
    {
        return "watermark <name>";
    }

    @Override
    public void onCommand(String[] args)
    {
        if (args.length < 1) {
            state(CommandState.ERROR);
            return;
        }

        String module = args[1];

        String result = module.trim();

        Infinity.CLIENT_NAME = result;

        Managers.MESSAGES.sendMessage("Watermark has been set to " + result, true);

        state(CommandState.PERFORMED);
    }

    @Override
    public String[] syntax(String string) {
        return new String[]{"<name>"};
    }
}
