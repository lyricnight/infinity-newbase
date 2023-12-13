package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.manager.Managers;

/**
 * @author vasler
 * friend command
 */

public class Friend extends Command {

    public Friend() {
        super("friend");
    }

    @Override
    public String theCommand()
    {
        return "friend <add/new/del/remove> <username>";
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 2) {
            switch (args[0]) {
                case "add", "new" -> {
                    Managers.FRIENDS.addFriend(args[1]);
                    state(CommandState.PERFORMED);
                    return;
                }
                case "del", "remove" -> {
                    Managers.FRIENDS.removeFriend(args[1]);
                    state(CommandState.PERFORMED);
                    return;
                }
            }
            state(CommandState.ERROR);
        }
        else
        {
            state(CommandState.ERROR);
        }
    }
}