package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.render.RenderPlayerModelEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ...
 */
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer implements IMinecraft {
    @Unique
    private float yaw, prevYaw, bodyYaw, prevBodyYaw, headYaw, prevHeadYaw, pitch, prevPitch;

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At(value = "HEAD"))
    private void onRenderHead(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        RenderPlayerModelEvent renderPlayerModelEvent = new RenderPlayerModelEvent(abstractClientPlayerEntity);
        EventBus.getInstance().post(renderPlayerModelEvent);
        yaw = abstractClientPlayerEntity.getYaw();
        prevYaw = abstractClientPlayerEntity.prevYaw;
        bodyYaw = abstractClientPlayerEntity.bodyYaw;
        prevBodyYaw = abstractClientPlayerEntity.prevBodyYaw;
        headYaw = abstractClientPlayerEntity.headYaw;
        prevHeadYaw = abstractClientPlayerEntity.prevHeadYaw;
        pitch = abstractClientPlayerEntity.getPitch();
        prevPitch = abstractClientPlayerEntity.prevPitch;
        if (renderPlayerModelEvent.isCancelled()) {
            abstractClientPlayerEntity.setYaw(renderPlayerModelEvent.getYaw());
            abstractClientPlayerEntity.prevYaw = renderPlayerModelEvent.getYaw();
            abstractClientPlayerEntity.setBodyYaw(renderPlayerModelEvent.getYaw());
            abstractClientPlayerEntity.prevBodyYaw = renderPlayerModelEvent.getYaw();
            abstractClientPlayerEntity.setHeadYaw(renderPlayerModelEvent.getYaw());
            abstractClientPlayerEntity.prevHeadYaw = renderPlayerModelEvent.getYaw();
            abstractClientPlayerEntity.setPitch(renderPlayerModelEvent.getPitch());
            abstractClientPlayerEntity.prevPitch = renderPlayerModelEvent.getPitch();
        }
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At(value = "TAIL"))
    private void onRenderTail(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        abstractClientPlayerEntity.setYaw(yaw);
        abstractClientPlayerEntity.prevYaw = prevYaw;
        abstractClientPlayerEntity.setBodyYaw(bodyYaw);
        abstractClientPlayerEntity.prevBodyYaw = prevBodyYaw;
        abstractClientPlayerEntity.setHeadYaw(headYaw);
        abstractClientPlayerEntity.prevHeadYaw = prevHeadYaw;
        abstractClientPlayerEntity.setPitch(pitch);
        abstractClientPlayerEntity.prevPitch = prevPitch;
    }
}
