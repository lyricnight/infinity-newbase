package club.lyric.infinity.asm;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.events.mc.chat.ChatSentEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author lyric
 * @link {EventManager}
 */

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessageHook(String message, CallbackInfo ci) {

        if (message.startsWith(Managers.COMMANDS.getPrefix()))
        {
            String[] arguments = message.replaceFirst(Managers.COMMANDS.getPrefix(), "").split(" ");

            boolean isCommand = false;

            for (Command commands : Managers.COMMANDS.getCommands())
            {

                if (commands.getCommand().equals(arguments[0]))
                {
                    commands.onCommand(arguments);

                    isCommand = true;

                    break;
                }
            }
            if (!isCommand)
            {
                ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown command. Try " + Managers.COMMANDS.getPrefix() + "commands for a list of available commands.");
            }
            ci.cancel();
        }


        ChatSentEvent event = new ChatSentEvent(message);
        EventBus.getInstance().post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "onEntityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityStatusHook(EntityStatusS2CPacket packet, CallbackInfo ci, Entity entity, int i)
    {
        Managers.OTHER.onTotemPop(entity);
    }
}