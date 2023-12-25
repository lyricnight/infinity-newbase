package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

/**
 * @author vasler
 * nojumping delay
 * @since 12/23/2023
 */
public class NoJumpDelay extends ModuleBase {
    public NoJumpDelay() {
        super("NoJumpDelay", "Removes the delay in between jumping.",  Category.MOVEMENT);
    }
}
