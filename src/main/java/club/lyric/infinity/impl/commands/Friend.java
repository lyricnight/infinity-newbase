package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.client.StringUtils;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * friend command
 */

public class Friend extends Command {

    public Friend() {
        super("friend");
    }

    @Override
    public String theCommand()
    {
        return "friend <add/del> <username> / list";
    }

    @Override
    public void onCommand(String[] args)
    {
        String friend = null;
        String task = null;
        if (args.length < 1)
        {
            state(CommandState.ERROR);
        }
        if (args.length > 2)
        {
            task = args[1];
            friend = args[2];
            if (friend == null || task == null) return;
        }
        if (args.length > 3)
        {
            state(CommandState.ERROR);
        }
        if (StringUtils.contains(task, "add"))
        {
            Managers.FRIENDS.addFriend(friend);
            state(CommandState.PERFORMED);
        }
        if (StringUtils.contains(task, "add"))
        {
            Managers.FRIENDS.removeFriend(friend);
            state(CommandState.PERFORMED);
        }
        if (StringUtils.contains(task, "list"))
        {
            ChatUtils.sendMessagePrivate("All your friends :");
            for (int i = 0; i <= Managers.FRIENDS.getFriends().size(); i++)
            {
                ChatUtils.sendMessagePrivate(Managers.FRIENDS.getFriends().get(i));
            }
            state(CommandState.PERFORMED);
        }
    }
}