package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.visual.Ambience;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author valser and lyric
 */
@Mixin(ClientWorld.class)
public abstract class MixinClientWorld implements IMinecraft {

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn()) {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void getCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn()) {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }

    @Inject(method = "addEntity", at = @At("HEAD"))
    public void addEntityHook(Entity entity, CallbackInfo ci) {
        if (Null.is()) return;
        if (entity instanceof PlayerEntity)
        {
            Managers.OTHER.onAddEntity((PlayerEntity) entity);
        }
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void removeEntityHook(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (Null.is()) return;
        if (mc.world.getEntityById(entityId) instanceof PlayerEntity)
        {
            Managers.OTHER.onRemoveEntity((PlayerEntity) mc.world.getEntityById(entityId));
        }
    }


}