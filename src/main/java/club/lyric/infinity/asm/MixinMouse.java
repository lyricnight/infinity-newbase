package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.client.gui.IMLoader;
import club.lyric.infinity.api.util.client.gui.Menu;
import club.lyric.infinity.api.util.client.gui.Tabs;
import club.lyric.infinity.impl.modules.client.ClickGui;
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
            double scrollPos = vertical * 30;
            if (IMLoader.isRendered(Menu.getInstance())) {
                for (Tabs tabsVar : Menu.getInstance().tabs) {
                    if (tabsVar.isFocused()) {
                        tabsVar.scrollPos -= (float) scrollPos;
                    }
                }
            }
        }
    }
}
