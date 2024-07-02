package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.mc.DeathEvent;
import club.lyric.infinity.impl.events.mc.movement.LocationEvent;
import club.lyric.infinity.impl.events.mc.movement.MotionEvent;
import club.lyric.infinity.impl.events.mc.movement.PlayerMovementEvent;
import club.lyric.infinity.impl.events.mc.update.UpdateWalkingPlayerEvent;
import club.lyric.infinity.impl.modules.movement.Sprint;
import club.lyric.infinity.manager.Managers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
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
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;
    @Shadow
    private boolean lastOnGround;
    @Shadow
    public Input input;

    @Shadow
    protected abstract void sendSprintingPacket();

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    private boolean lastSneaking;

    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    protected abstract boolean isCamera();

    @Shadow
    public int ticksSinceLastPositionPacketSent;

    @Shadow
    private boolean autoJumpEnabled;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHook(CallbackInfo ci) {
        Managers.ROTATIONS.update();
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
    public void tickMovement(MovementType movementType, Vec3d movement, CallbackInfo callbackInfo) {
        MotionEvent event = new MotionEvent(movement.x, movement.y, movement.z, 0);
        EventBus.getInstance().post(event);
        if (event.isCancelled()) {
            super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
            callbackInfo.cancel();
        }
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean modifyMovement(boolean original) {
        return Managers.MODULES.getModuleFromClass(Sprint.class).isOn()
                && Managers.MODULES.getModuleFromClass(Sprint.class).mode.is("Rage")
                && (mc.player.forwardSpeed != 0.0f || mc.player.sidewaysSpeed != 0.0f) || input.hasForwardMovement();
    }

    @Inject(method = "sendMovementPackets", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPacketsHead(CallbackInfo info) {
        eventGlobal = new LocationEvent(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround());
        EventBus.getInstance().post(eventGlobal);
        double x = eventGlobal.getX();
        double y = eventGlobal.getY();
        double z = eventGlobal.getZ();
        float yaw = eventGlobal.getYaw();
        float pitch = eventGlobal.getPitch();
        boolean onGround = eventGlobal.isOnGround();
        if (eventGlobal.isCancelled()) {
            info.cancel();
            sendSprintingPacket();
            boolean sneak = isSneaking();
            if (sneak != lastSneaking) {
                ClientCommandC2SPacket.Mode packet = sneak ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
                networkHandler.sendPacket(new ClientCommandC2SPacket(this, packet));
                lastSneaking = sneak;
            }
            if (isCamera()) {
                double d = x - lastX;
                double e = y - lastBaseY;
                double f = z - lastZ;
                double g = yaw - lastYaw;
                double h = pitch - lastPitch;
                ++ticksSinceLastPositionPacketSent;
                boolean bl2 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || ticksSinceLastPositionPacketSent >= 20;
                boolean bl3 = g != 0.0 || h != 0.0;
                if (hasVehicle()) {
                    Vec3d vec3d = getVelocity();
                    networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, getYaw(), getPitch(), onGround));
                    bl2 = false;
                } else if (bl2 && bl3) {
                    networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround));
                } else if (bl2) {
                    networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
                } else if (bl3) {
                    networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, onGround));
                } else if (lastOnGround != isOnGround()) {
                    networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(onGround));
                }
                if (bl2) {
                    lastX = x;
                    lastBaseY = y;
                    lastZ = z;
                    ticksSinceLastPositionPacketSent = 0;
                }
                if (bl3) {
                    lastYaw = yaw;
                    lastPitch = pitch;
                }
                lastOnGround = onGround;
                autoJumpEnabled = mc.options.getAutoJump().getValue();
            }
        }
    }

    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    private void hookMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        PlayerMovementEvent event = new PlayerMovementEvent(movementType, movement);
        EventBus.getInstance().post(event);
        if (event.isCancelled()) {
            super.move(event.getType(), event.getMovement());
            ci.cancel();
        }
    }
}