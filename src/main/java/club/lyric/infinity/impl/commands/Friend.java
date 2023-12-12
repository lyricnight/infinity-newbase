package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

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
                    return;
                }
                case "del", "remove" -> {
                    Managers.FRIENDS.removeFriend(args[1]);
                    return;
                }
            }
            ChatUtils.sendMessagePrivate("Unknown formatting, try to use this format: friend <add/new/del/remove> <username>");
        }
    }
}