package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.mc.DeathEvent;
import club.lyric.infinity.impl.events.mc.movement.LocationEvent;
import club.lyric.infinity.impl.events.mc.movement.MotionEvent;
import club.lyric.infinity.impl.events.mc.movement.PlayerMovementEvent;
import club.lyric.infinity.impl.events.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.manager.Managers;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 * clusterfuck
 */
@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements IMinecraft {

    @Unique
    private LocationEvent eventGlobal;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract void autoJump(float dx, float dz);


    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHook(CallbackInfo ci) {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onUpdate);
        if (mc.world == null) return;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            EventBus.getInstance().post(new DeathEvent(player));
        }
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void tickHooker(CallbackInfo ci) {
        EventBus.getInstance().post(new UpdateWalkingPlayerEvent(0));
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;tickables:Ljava/util/List;", shift = At.Shift.AFTER))
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

    @Inject(method = "sendMovementPackets", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPacketsHead(CallbackInfo info) {
        eventGlobal = new LocationEvent.Pre(this.getX(), this.getBoundingBox().minY, this.getZ(), this.getYaw(), this.getPitch(), this.isOnGround());
        EventBus.getInstance().post(eventGlobal);
        if (eventGlobal.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    private void hookMove(MovementType movementType, Vec3d movement, CallbackInfo ci)
    {
        final PlayerMovementEvent playerMoveEvent = new PlayerMovementEvent(movementType, movement);
        EventBus.getInstance().post(playerMoveEvent);
        if (playerMoveEvent.isCancelled())
        {
            ci.cancel();
            super.move(movementType, playerMoveEvent.getMovement());
        }
    }

    @Inject(method = "sendMovementPackets", at = @At(value = "RETURN"))
    private void onSendMovementPacketsTail(CallbackInfo info) {
        LocationEvent.Post event = new LocationEvent.Post(eventGlobal.getX(), eventGlobal.getY(), eventGlobal.getZ(), eventGlobal.getYaw(), eventGlobal.getPitch(), eventGlobal.isOnGround());
        event.setCancelled(eventGlobal.isCancelled());
        EventBus.getInstance().post(event);
    }
}