package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo callbackInfo)
    {
        EventBus.getInstance().post(new TickEvent(0));
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickPost(CallbackInfo callbackInfo)
    {
        EventBus.getInstance().post(new TickEvent(1));
    }
}


