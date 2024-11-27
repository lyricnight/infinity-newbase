package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.gui.IMLoader;
import club.lyric.infinity.api.gui.Menu;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.impl.modules.client.Configuration;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            if ((Managers.MODULES.getModuleFromClass(Configuration.class).showSettings() && IMLoader.isRendered(Menu.getInstance())) && key != GLFW.GLFW_KEY_ESCAPE && key != Managers.MODULES.getModuleFromClass(ClickGUI.class).getBind() && key != GLFW.GLFW_KEY_BACKSPACE)
            {
                info.cancel();
                return;
            }
            KeyPressEvent keyPressEvent = new KeyPressEvent(key, action);
            EventBus.getInstance().post(keyPressEvent);
            if (keyPressEvent.isCancelled()) {
                info.cancel();
            }
        }
    }
}
