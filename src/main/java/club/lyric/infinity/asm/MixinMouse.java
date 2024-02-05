package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.client.gui.IMLoader;
import club.lyric.infinity.api.util.client.gui.InfinityGUI;
import club.lyric.infinity.impl.modules.client.clickgui.ClickGui;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 * for gui
 */
@Mixin(Mouse.class)
public class MixinMouse {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (Managers.MODULES.getModuleFromClass(ClickGui.class).isOn()) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (Managers.MODULES.getModuleFromClass(ClickGui.class).isOn()) {
            double scrollY = vertical * 30;
            if (IMLoader.isRendered(InfinityGUI.getInstance())) {
                InfinityGUI.getInstance().locY -= scrollY;
            }
            ci.cancel();
        }
    }
}
