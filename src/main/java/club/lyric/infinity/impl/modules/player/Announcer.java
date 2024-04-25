package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

public class Announcer extends ModuleBase {
    public Announcer() {
        super("Announcer", "Announces certain information in chat.", Category.Player);
    }
}
