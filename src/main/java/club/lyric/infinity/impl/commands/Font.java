package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.modules.client.Fonts;
import club.lyric.infinity.manager.Managers;

/**
 * @author vasler
 */
public class Font extends Command
{

    public Font()
    {
        super("font");
    }

    @Override
    public String theCommand()
    {
        return "font <name>";
    }

    @Override
    public void onCommand(String[] args)
    {
        if (args.length < 1)
        {
            state(CommandState.ERROR);
            return;
        }

        String module = args[1];
        String result = module.trim();

        if (Managers.MODULES.getModuleFromClass(Fonts.class).isValid(result))
        {
            Managers.MODULES.getModuleFromClass(Fonts.class).setFont(result);
            ChatUtils.sendMessagePrivate("Font has been set to " + result);
        }
        else
        {
            state(CommandState.ERROR);
        }
    }

    @Override
    public String[] syntax(String string) {
        return new String[]{"<name>"};
    }
}
