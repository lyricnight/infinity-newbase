package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.TickEvent;
import club.lyric.infinity.asm.accessors.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo callbackInfo)
    {
        EventBus.getInstance().post(new TickEvent.Pre());
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickPost(CallbackInfo callbackInfo)
    {
        EventBus.getInstance().post(new TickEvent.Post());
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(CallbackInfo callbackInfo)
    {
        Managers.TEXT.init();
    }

    @Inject(method = "close", at = @At(value = "HEAD"))
    private void close(CallbackInfo callbackInfo)
    {
        Managers.CONFIG.saveConfig();
    }
    @Inject(method = "cleanUpAfterCrash", at = @At(value = "HEAD"))
    public void cleanUpAfterCrash(CallbackInfo ci) {
        Managers.CONFIG.saveConfig();
    }
}


