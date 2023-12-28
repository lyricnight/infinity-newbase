package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.ducks.IChatHudLine;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.impl.modules.render.Chat;
import club.lyric.infinity.manager.Managers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

/**
 * @author lyric
 * for overwrite messages
 */
@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud {

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
    public abstract void addMessage(Text message);

    @Shadow
    protected abstract void addMessage(Text message, @Nullable MessageSignatureData messageSignatureData, int ticks, @Nullable MessageIndicator messageIndicator, boolean refresh);

    @Override
    public void infinity$add(Text text, int id) {
        idConcurrent = id;
        addMessage(text);
        idConcurrent = 0;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(Text message, MessageSignatureData messageSignatureData, int ticks, MessageIndicator messageIndicator, boolean refresh, CallbackInfo callbackInfo) {
        ((IChatHudLine) (Object) visibleMessages.get(0)).infinity$setId(idConcurrent);
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(Text message, MessageSignatureData messageSignatureData, int ticks, MessageIndicator messageIndicator, boolean refresh, CallbackInfo callbackInfo) {
        ((IChatHudLine) (Object) messages.get(0)).infinity$setId(idConcurrent);
    }
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", cancellable = true)
    private void onAddMessage(Text message, @Nullable MessageSignatureData signature, int ticks, @Nullable MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        if (addText)
        {
            return;
        }

        ReceiveChatEvent event = new ReceiveChatEvent(message, indicator, idConcurrent);
        EventBus.getInstance().post(event);
        if (event.isCancelled()) info.cancel();
        else {
            visibleMessages.removeIf(msg -> ((IChatHudLine) (Object) msg).infinity$getId() == idConcurrent && idConcurrent != 0);

            for (int i = messages.size() - 1; i > -1; i--) {
                if (((IChatHudLine) (Object) messages.get(i)).infinity$getId() == idConcurrent && idConcurrent != 0) {
                    messages.remove(i);
                    Managers.MODULES.getModuleFromClass(Chat.class).lines.removeInt(i);
                }
            }

            if (event.hasBeenAdded()) {
                info.cancel();

                addText = true;
                addMessage(event.getMessage(), signature, ticks, event.getIndicator(), refresh);
                addText = false;
            }
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;isChatFocused()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onBreakChatMessageLines(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci, int i, List<OrderedText> list) {
        Managers.MODULES.getModuleFromClass(Chat.class).lines.add(0, list.size());
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;messages:Ljava/util/List;")), at = @At(value = "INVOKE", target = "Ljava/util/List;remove(I)Ljava/lang/Object;"))
    private void onRemoveMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        int size = Managers.MODULES.getModuleFromClass(Chat.class).lines.size();

        while (size > 100) {
            Managers.MODULES.getModuleFromClass(Chat.class).lines.removeInt(size - 1);
            size--;
        }
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;"))
    private MessageIndicator onRender_modifyIndicator(MessageIndicator indicator) {
        return Managers.MODULES.getModuleFromClass(Chat.class).remove.getValue() ? null : indicator;
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
