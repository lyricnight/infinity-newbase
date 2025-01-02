package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.manager.Managers;

/**
 * @author vasler
 */
public final class Timer extends ModuleBase {
    public NumberSetting factor = new NumberSetting("Factor", this, 1.0888f, 0.1f, 4.0f, 0.01f);
    public Timer()
    {
        super("Timer", "aa", Category.PLAYER);
    }

    @Override
    public void onUpdate()
    {
        Managers.TIMER.set(factor.getFValue());
    }

    @Override
    public void onDisable()
    {
        Managers.TIMER.reset();
    }
}
