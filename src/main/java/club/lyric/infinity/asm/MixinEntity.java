package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.mc.EntityMovementEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author lyric
 */

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Unique
    private EntityMovementEvent event;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void moveEntityHookPre(MovementType type, Vec3d vec3d, CallbackInfo callbackInfo) {
            event = new EntityMovementEvent(type, vec3d.x, vec3d.y, vec3d.z);
            EventBus.getInstance().post(event);
            if (event.isCancelled()) {
                callbackInfo.cancel();
            }
    }

    @ModifyVariable(method = "move", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private Vec3d modifyVec(Vec3d vec3d)
    {
        if(event != null)
        {
            vec3d = new Vec3d(event.getX(), event.getY(), event.getZ());
            return vec3d;
        }
        return vec3d;
    }

    @Inject(method = "move", at = @At(value = "RETURN"))
    public void moveEntityHookPost(MovementType type, Vec3d vec3d, CallbackInfo info) {
        this.event = null;
    }
}
