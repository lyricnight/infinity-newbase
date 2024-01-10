package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.MotionEvent;
import club.lyric.infinity.api.event.mc.update.UpdateEvent;
import club.lyric.infinity.api.event.mc.update.UpdateWalkingPlayerEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHook(CallbackInfo ci) {
        EventBus.getInstance().post(new UpdateEvent());
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void tickHooker(CallbackInfo ci) {
        EventBus.getInstance().post(new UpdateWalkingPlayerEvent(0));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", shift = At.Shift.AFTER))
    private void tickHookering(CallbackInfo ci) {
        EventBus.getInstance().post(new UpdateWalkingPlayerEvent(1));
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
    public void tickMovement(MovementType movementType, Vec3d movement, CallbackInfo callbackInfo)
    {
        MotionEvent event = new MotionEvent(movement.x, movement.y, movement.z, 0);
        EventBus.getInstance().post(event);
        if (event.isCancelled())
        {
            super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
            callbackInfo.cancel();
        }
    }
}