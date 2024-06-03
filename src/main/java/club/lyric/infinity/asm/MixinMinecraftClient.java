package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Nullables;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements IMinecraft {

    @Shadow
    private IntegratedServer server;

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
    }

    @ModifyArg(method = "updateWindowTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setTitle(Ljava/lang/String;)V"))
    public String modifyUpdateWindowTitle(String title)
    {

        StringBuilder stringBuilder = new StringBuilder(Infinity.CLIENT_NAME);

        if (MinecraftClient.getModStatus().isModded())
        {
            stringBuilder.append("*");
        }

        stringBuilder.append(" ");
        stringBuilder.append(SharedConstants.getGameVersion().getName());

        ClientPlayNetworkHandler clientPlayNetworkHandler = this.getNetworkHandler();

        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.getConnection().isOpen())
        {
            stringBuilder.append(" - ");
            ServerInfo serverInfo = this.getCurrentServerEntry();
            if (this.server != null && !this.server.isRemote())
            {
                stringBuilder.append(I18n.translate("Singleplayer", new Object[0]));
            }
            else if (serverInfo != null && serverInfo.isRealm())
            {
                stringBuilder.append(I18n.translate("title.multiplayer.realms", new Object[0]));
            }
            else if (server == null && (serverInfo == null || !serverInfo.isLocal()))
            {
                stringBuilder.append(I18n.translate("title.multiplayer.other", new Object[0]));
            }
            else
            {
                stringBuilder.append(I18n.translate("title.multiplayer.lan", new Object[0]));
            }

        }

        return stringBuilder.toString();
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

    @Nullable
    public ClientPlayNetworkHandler getNetworkHandler()
    {
        return mc.player == null ? null : mc.player.networkHandler;
    }

    @Nullable
    public ServerInfo getCurrentServerEntry()
    {
        return Nullables.map(this.getNetworkHandler(), ClientPlayNetworkHandler::getServerInfo);
    }
}


