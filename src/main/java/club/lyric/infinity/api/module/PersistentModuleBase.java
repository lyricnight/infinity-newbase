package club.lyric.infinity.api.module;

import club.lyric.infinity.api.util.client.chat.ChatUtils;

/**
 * @author lyric
 * for persistent modules
 */
public class PersistentModuleBase extends ModuleBase {
    public PersistentModuleBase(String name, String description, Category category)
    {
        super(name, description, category);
        setEnabled(true);
    }

    @Override
    public void disable()
    {
        ChatUtils.sendMessagePrivate("You can't disable this module.");
    }
}
