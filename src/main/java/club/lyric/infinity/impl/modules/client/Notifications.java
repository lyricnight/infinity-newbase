package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;


/**
 * @author lyric & vasler
 */
public final class Notifications extends PersistentModuleBase {
    public BooleanSetting totemPops =
            new BooleanSetting(
                    "TotemPops",
                    true,
                    this
            );

    public BooleanSetting toggled =
            new BooleanSetting(
                    "Toggled",
                    true,
                    this
            );

    public BooleanSetting render =
            new BooleanSetting(
                    "Render",
                    true,
                    this
            );

    public Notifications() {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }
}
