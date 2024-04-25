package club.lyric.infinity.api.util.client.discord;

import club.lyric.infinity.api.util.client.discord.callback.*;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DiscordEventHandlers extends Structure {
    public DisconnectedCallback disconnected;
    public JoinRequestCallback joinRequest;
    public SpectateGameCallback spectateGame;
    public ReadyCallback ready;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;

    protected List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
}
