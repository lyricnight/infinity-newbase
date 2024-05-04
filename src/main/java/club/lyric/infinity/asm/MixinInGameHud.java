package club.lyric.infinity.asm;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.modules.client.HUD;
import club.lyric.infinity.impl.modules.visual.Chat;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author vasler
 */
@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(module -> module.onRender2D(context));
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        RenderSystem.enableDepthTest();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "HEAD"), cancellable = true)
    private void hookRenderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
        if (Managers.MODULES.getModuleFromClass(HUD.class).effectHud.is("Remove")) {
            ci.cancel();
        }
    }

    /**
     * @author lyric
     * @param info -> callback
     */
    @Inject(method = "clear", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V"), cancellable = true)
    private void onClear(CallbackInfo info) {
        if (Managers.MODULES.getModuleFromClass(Chat.class).keep.value()) {
            info.cancel();
        }
    }
}
