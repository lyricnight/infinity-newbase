package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.EntityMovementEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */

@Mixin(Entity.class)
public abstract class MixinEntity {

    private EntityMovementEvent event;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void moveEntityHookPre(MovementType type, Vec3d vec3d, CallbackInfo callbackInfo) {
        //might be PlayerEntity
        if (ClientPlayerEntity.class.isInstance(this)) {
            event = new EntityMovementEvent(type, vec3d.x, vec3d.y, vec3d.z);
            EventBus.getInstance().post(event);
            if (event.isCancelled()) {
                callbackInfo.cancel();
            }
        }
    }

    @ModifyVariable(method = "move", at = @At(value = "HEAD"), ordinal = 0)
    private double setX(double x) {
        return this.event != null ? this.event.getX() : x;
    }

    @ModifyVariable(method = "move", at = @At(value = "HEAD"), ordinal = 1)
    private double setY(double y) {
        return this.event != null ? this.event.getY() : y;
    }

    @ModifyVariable(method = "move", at = @At(value = "HEAD"), ordinal = 2)
    private double setZ(double z) {
        return this.event != null ? this.event.getZ() : z;
    }

    @Inject(method = "move", at = @At(value = "RETURN"))
    public void moveEntityHookPost(MovementType type, Vec3d vec3d, CallbackInfo info) {
        this.event = null;
    }
}
