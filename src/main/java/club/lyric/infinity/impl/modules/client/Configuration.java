package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.manager.Managers;

/**
 * @author lyric
 * for gui config
 */
public final class Configuration extends PersistentModuleBase {
    public Configuration()
    {
        super("Configuration", "Manages configs.", Category.Client);
    }
    @Override
    public void renderSettings()
    {
        Managers.CONFIG.renderGui();
    }
}
