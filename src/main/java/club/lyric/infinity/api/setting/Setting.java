package club.lyric.infinity.api.setting;

import club.lyric.infinity.api.module.ModuleBase;
import lombok.Getter;

/**
 * @author lyric
 * make things simple
 */
@Getter
public abstract class Setting {
    /**
     * name of our setting.
     */
    protected String name;
    /**
     * moduleBase that the setting belongs to.
     */
    protected ModuleBase moduleBase;

    protected boolean visibility;
}

