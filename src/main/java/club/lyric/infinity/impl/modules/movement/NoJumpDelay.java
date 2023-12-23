package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.asm.LivingEntityAccessor;

public class NoJumpDelay extends ModuleBase {

    public NoJumpDelay() {
        super("NoJumpDelay", "Removes the delay in between jumping.",  Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ((LivingEntityAccessor) mc.player).setJumpCooldown(0);
    }
}
