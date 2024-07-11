package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.exploit.KickPrevent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.network.packet.c2s.play.MessageAcknowledgmentC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 * @see club.lyric.infinity.impl.modules.exploit.KickPrevent
 */
@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
    @Inject(method = "onMessageAcknowledgment", at = @At(value = "HEAD"), cancellable = true)
    private void onMessageAckknowledgement(MessageAcknowledgmentC2SPacket packet, CallbackInfo ci)
    {
        if (Managers.MODULES.getModuleFromClass(KickPrevent.class).textFormat.value())
        {
            ci.cancel();
        }
    }
}
