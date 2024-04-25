package club.lyric.infinity.api.util.client.discord.callback;

import club.lyric.infinity.api.util.client.discord.DiscordUser;
import com.sun.jna.Callback;

public interface ReadyCallback extends Callback {
     void apply(DiscordUser p0);
}
