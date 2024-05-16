package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;

/**
 * @author lyric
 */
public class Notifications extends PersistentModuleBase
{
    public BooleanSetting totemPops =
            new BooleanSetting(
            "TotemPops",
            false,
            this
    );
    public BooleanSetting enable =
            new BooleanSetting(
            "Enabled",
            true,
            this
    );
    public BooleanSetting disable =
            new BooleanSetting(
            "Disabled",
            true,
            this
    );

    public BooleanSetting visualRange =
            new BooleanSetting(
            "VisualRange",
            false,
            this
    );

    public Notifications()
    {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }
}
