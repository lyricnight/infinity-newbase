package club.lyric.infinity.asm;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.events.mc.chat.ChatSentEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

            for (Command commands : Managers.COMMANDS.getCommands()) {
                if (commands.getCommand().equals(arguments[0])) {
                    commands.onCommand(arguments);

                    isCommand = true;

                    break;
                }
            }
            if (!isCommand) {
                ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown command. Try " + Managers.COMMANDS.getPrefix() + "commands for a list of available commands.");
            }
            ci.cancel();
        }


        ChatSentEvent event = new ChatSentEvent(message);
        EventBus.getInstance().post(event);
        if (event.isCancelled())
            ci.cancel();
    }
}