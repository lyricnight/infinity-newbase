package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;


/**
 * @author lyric & vasler
 */
public class Notifications extends PersistentModuleBase {
    public BooleanSetting totemPops =
            new BooleanSetting(
                    "TotemPops",
                    false,
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
                    false,
                    this
            );

    public Notifications() {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }
}
