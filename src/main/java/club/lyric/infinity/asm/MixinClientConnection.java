package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.impl.modules.exploit.KickPrevent;
import club.lyric.infinity.manager.Managers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.handler.PacketEncoderException;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.TimeoutException;

/**
 * @author lyric
 * @see PacketEvent
 */
@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Shadow
    private Channel channel;

    @Shadow
    @Final
    private NetworkSide side;

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (channel.isOpen() && packet != null) {
            try {
                if (packet instanceof CommandSuggestionsS2CPacket commandSuggestionsS2CPacket && commandSuggestionsS2CPacket.getCompletionId() == 1337)
                {
                    Managers.SERVER.ping = (int) (System.currentTimeMillis() - Managers.SERVER.responseTime);
                }
                PacketEvent.Receive event = new PacketEvent.Receive(packet);
                EventBus.getInstance().post(event);
                if (event.isCancelled())
                {
                    ci.cancel();
                }
            } catch (Exception e) {
                Infinity.LOGGER.atError();
            }
        }
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    private void sendImmediately(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        if (this.side != NetworkSide.CLIENTBOUND) return;
        try {
            PacketEvent.Send event = new PacketEvent.Send(packet);
            EventBus.getInstance().post(event);
            if (event.isCancelled())
            {
                ci.cancel();
            }
        } catch (Exception e) {
            Infinity.LOGGER.atError();
        }
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exceptionCaught(ChannelHandlerContext context, Throwable throwable, CallbackInfo ci) {
        if (!(throwable instanceof TimeoutException) && !(throwable instanceof PacketEncoderException) && Managers.MODULES.getModuleFromClass(KickPrevent.class).isOn()) {
            ChatUtils.sendMessagePrivateColored(Formatting.RED + "[KickPrevent] Infinity caught an exception in the network stack: " + throwable.getMessage());
            ci.cancel();
        }
    }
}
