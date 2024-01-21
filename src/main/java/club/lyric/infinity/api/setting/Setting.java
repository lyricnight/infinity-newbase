package club.lyric.infinity.api.setting;

import club.lyric.infinity.api.module.ModuleBase;

/**
 * @author lyric
 * make things simple
 */
public abstract class Setting {
    /**
     * name of our setting.
     */
    protected String name;

    /**
     * moduleBase that the setting belongs to.
     */
    protected ModuleBase moduleBase;

    public String getName() {
        return name;
    }

    public ModuleBase getModuleBase() {
        return moduleBase;
    }
}

