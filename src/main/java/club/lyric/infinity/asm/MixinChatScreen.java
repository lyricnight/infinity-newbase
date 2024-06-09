package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.visual.Chat;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * @author lyric
 * @see club.lyric.infinity.impl.modules.visual.Chat
 */
@Mixin(value = ChatScreen.class)
public abstract class MixinChatScreen implements IMinecraft {
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void onInit(CallbackInfo info) {
        if (Managers.MODULES.getModuleFromClass(Chat.class).infiniteMessages.value()) chatField.setMaxLength(Integer.MAX_VALUE);
    }

}