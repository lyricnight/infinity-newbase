package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.DiscordUtil;

public class DiscordRPC extends ModuleBase {

    public DiscordRPC() {
        super("DiscordRPC", "Toggles the Discord Presence", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        DiscordUtil.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DiscordUtil.stop();
    }
}
