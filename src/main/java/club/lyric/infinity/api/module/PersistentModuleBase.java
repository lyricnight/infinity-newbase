package club.lyric.infinity.api.module;

import club.lyric.infinity.api.util.client.chat.ChatUtils;

/**
 * @author lyric
 * for persistent modules
 */
public class PersistentModuleBase extends ModuleBase {
    public PersistentModuleBase(String name, String description, Category category) {
        super(name, description, category);
        setEnabled(true);
        bind = null;
    }

    @Override
    public int getBind() {
        return -1;
    }

    @Override
    public void setBind(int key) {}

    @Override
    public void disable() {
        ChatUtils.sendMessagePrivate("You can't disable this module.");
    }
}
