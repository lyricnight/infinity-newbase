package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements IMinecraft {

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo callbackInfo)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onTickPre);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickPost(CallbackInfo callbackInfo)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onTickPost);
        Managers.TIMER.update();
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(CallbackInfo callbackInfo)
    {
        Managers.TEXT.init();
        if (mc.getWindow() != null) {
            mc.getWindow().setTitle("Infinity" + " - " + mc.getVersionType() + " " + SharedConstants.getGameVersion().getName());
        }
    }

    @Inject(method = "close", at = @At(value = "HEAD"))
    private void close(CallbackInfo callbackInfo)
    {
        Managers.unload();
    }

    @Inject(method = "cleanUpAfterCrash", at = @At(value = "HEAD"))
    public void cleanUpAfterCrash(CallbackInfo ci)
    {
        Managers.unload();
    }
}


