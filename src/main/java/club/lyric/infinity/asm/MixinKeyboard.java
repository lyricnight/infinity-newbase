package club.lyric.infinity.asm;


import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            KeyPressEvent keyPressEvent = new KeyPressEvent(key, action);
            EventBus.getInstance().post(keyPressEvent);
            if (keyPressEvent.isCancelled()) {
                info.cancel();
            }
        }
    }
}
