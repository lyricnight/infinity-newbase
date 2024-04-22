package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.discord.DiscordUtil;

/**
 * @author railhack
 */
public final class RichPresence extends ModuleBase {
    public RichPresence() {
        super("RichPresence", "Toggles the Discord Presence", Category.Client);
    }
    public BooleanSetting ip = new BooleanSetting("ShowIP", false, this);
    public BooleanSetting ign = new BooleanSetting("ShowIGN", false, this);

    @Override
    public void onEnable()
    {
        DiscordUtil.start();
    }

    @Override
    public void onDisable()
    {
        DiscordUtil.stop();
    }
}
