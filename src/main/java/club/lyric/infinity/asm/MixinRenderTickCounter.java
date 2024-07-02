package club.lyric.infinity.asm;

import club.lyric.infinity.manager.Managers;
import net.minecraft.client.render.RenderTickCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author lyric
 */
@Mixin(RenderTickCounter.class)
public class MixinRenderTickCounter {
    @Shadow
    public float lastFrameDuration;

    @Inject(method = "beginRenderTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J", opcode = Opcodes.PUTFIELD))
    private void beginRenderTick(long a, CallbackInfoReturnable<Integer> info) {
        lastFrameDuration *= Managers.TIMER.getTimer();
    }
}
