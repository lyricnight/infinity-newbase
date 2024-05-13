package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

public class SafeWalk extends ModuleBase {

    public SafeWalk()
    {
        super("SafeWalk", "Removes clipping once you reach a ledge.", Category.Movement);
    }
}
