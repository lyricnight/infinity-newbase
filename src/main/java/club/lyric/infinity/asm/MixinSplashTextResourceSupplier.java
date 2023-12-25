package club.lyric.infinity.asm;

import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author lyric
 */

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier {
    @Unique
    private boolean applied = true;
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onApply(CallbackInfoReturnable<SplashTextRenderer> cir) {
        if (Managers.CONFIG == null || Managers.MODULES == null) return;

        if (applied) cir.setReturnValue(new SplashTextRenderer("Welcome to Infinity."));
        applied = !applied;
    }
}
