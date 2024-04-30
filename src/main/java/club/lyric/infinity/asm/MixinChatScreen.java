package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.modules.render.Chat;
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
 * @see club.lyric.infinity.impl.modules.render.Chat
 */
@Mixin(value = ChatScreen.class)
public abstract class MixinChatScreen {
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void onInit(CallbackInfo info) {
        if (Managers.MODULES.getModuleFromClass(Chat.class).infiniteMessages.value()) chatField.setMaxLength(Integer.MAX_VALUE);
    }
}