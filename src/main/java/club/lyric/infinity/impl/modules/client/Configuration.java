package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends ModuleBase {
    public Configuration()
    {
        super("Configuration", "Manages configs.", Category.Client);
    }

    private final List<String> names = new ArrayList<>();

    @Override
    public void renderSettings()
    {
        Managers.CONFIG.renderGui();
    }
}
