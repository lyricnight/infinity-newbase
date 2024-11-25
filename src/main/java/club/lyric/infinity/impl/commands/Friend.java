package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.util.client.render.text.StringUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

/**
 * @author lyric
 * friend command
 */
public final class Friend extends Command implements IMinecraft {

    private final MinecraftServer server;

    public Friend() {
        super("friend");
        this.server = mc.player != null && mc.world != null ? mc.player.getServer() : null;
    }

    @Override
    public String theCommand() {
        return "friend <add/del> <username>";
    }

    @Override
    public void onCommand(String[] args) {
        String friend = null;
        String task = null;
        if (args.length < 1) {
            state(CommandState.ERROR);
            return;
        }
        if (args.length > 2) {
            task = args[1];
            friend = args[2];
            if (friend == null || task == null) return;
        }
        if (args.length > 3) {
            state(CommandState.ERROR);
            return;
        }
        if (StringUtils.contains(task, "add")) {
            Managers.FRIENDS.addFriend(friend);
            state(CommandState.PERFORMED);
        }
        if (StringUtils.contains(task, "del")) {
            Managers.FRIENDS.removeFriend(friend);
            state(CommandState.PERFORMED);
        }
    }

    @Override
    public String[] syntax(String string)
    {

        return switch (string)
        {
            case "add", "del" -> server == null ? new String[]{"<username>"} :getOnlineUsernames();

            default -> new String[]{"<add/del>"};
        };
    }

    private String[] getOnlineUsernames() {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        return players.stream().map(ServerPlayerEntity::getName).toArray(String[]::new);
    }
}