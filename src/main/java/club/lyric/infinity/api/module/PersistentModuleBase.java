package club.lyric.infinity.api.module;

import club.lyric.infinity.manager.Managers;

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
        Managers.MESSAGES.sendMessage("You can't disable this module.", false);
    }
}
