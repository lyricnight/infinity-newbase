package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.client.chat.ChatUtils;

public class Watermark extends Command
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

        ChatUtils.sendMessagePrivate("Watermark has been set to " + result);
    }
}
