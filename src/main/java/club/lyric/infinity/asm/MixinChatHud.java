package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.ducks.IChatHudLine;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.mc.chat.ReceiveChatEvent;
import club.lyric.infinity.impl.modules.visual.Chat;
import club.lyric.infinity.manager.Managers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author lyric
 * for overwrite messages
 */
@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud, IMinecraft {
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    @Final
    private List<ChatHudLine> messages;
    @Unique
    private int idConcurrent;
    @Unique
    private boolean addText;
    @Shadow
    public abstract void addMessage(Text message, @Nullable MessageSignatureData messageSignatureData, @Nullable MessageIndicator messageIndicator);
    @Shadow
    public abstract void addMessage(Text message);


    @Override
    public void infinity$add(Text text, int id) {
        idConcurrent = id;
        addMessage(text);
        idConcurrent = 0;
    }

    @Inject(method = "addVisibleMessage", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(ChatHudLine message, CallbackInfo ci) {
        ((IChatHudLine) (Object) visibleMessages.getFirst()).infinity$setId(idConcurrent);
    }

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(ChatHudLine message, CallbackInfo ci) {
        ((IChatHudLine) (Object) messages.getFirst()).infinity$setId(idConcurrent);
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (addText) {
            return;
        }

        ReceiveChatEvent event = new ReceiveChatEvent(message, indicator, idConcurrent);
        EventBus.getInstance().post(event);
        if (event.isCancelled()) ci.cancel();
        else {
            visibleMessages.removeIf(msg -> ((IChatHudLine) (Object) msg).infinity$getId() == idConcurrent && idConcurrent != 0);

            for (int i = messages.size() - 1; i > -1; i--) {
                if (((IChatHudLine) (Object) messages.get(i)).infinity$getId() == idConcurrent && idConcurrent != 0) {
                    messages.remove(i);
                    Managers.MODULES.getModuleFromClass(Chat.class).lines.removeInt(i);
                }
            }

            if (event.hasBeenAdded()) {
                ci.cancel();

                addText = true;
                addMessage(event.getMessage(), signatureData, event.getIndicator());
                addText = false;
            }
        }
    }


    @Inject(method = "addVisibleMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;isChatFocused()Z"))
    private void onBreakChatMessageLines(ChatHudLine message, CallbackInfo ci, @Local List<OrderedText> list) {
        Managers.MODULES.getModuleFromClass(Chat.class).lines.addFirst(list.size());
    }

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(I)Ljava/lang/Object;"))
    private void onRemoveMessage(ChatHudLine message, CallbackInfo ci) {
        int size = Managers.MODULES.getModuleFromClass(Chat.class).lines.size();

        while (size > 100) {
            Managers.MODULES.getModuleFromClass(Chat.class).lines.removeLast();
            size--;
        }
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;"))
    private MessageIndicator onRender_modifyIndicator(MessageIndicator indicator) {
        return Managers.MODULES.getModuleFromClass(Chat.class).remove.value() ? null : indicator;
    }

    @Inject(method = "clear", at = @At("HEAD"))
    private void onClear(boolean clearHistory, CallbackInfo ci) {
        Managers.MODULES.getModuleFromClass(Chat.class).lines.clear();
    }

    @Inject(method = "refresh", at = @At("HEAD"))
    private void onRefresh(CallbackInfo ci) {
        Managers.MODULES.getModuleFromClass(Chat.class).lines.clear();
    }
}
