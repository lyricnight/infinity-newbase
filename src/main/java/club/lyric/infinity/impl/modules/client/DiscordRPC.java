package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

public final class DiscordRPC extends ModuleBase {

    public DiscordRPC() {
        super("DiscordRPC", "Toggles the Discord Presence", Category.Client);
    }
}
