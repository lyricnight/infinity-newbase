package club.lyric.infinity.asm;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.mc.item.PearlEvent;
import club.lyric.infinity.impl.events.render.RenderEntityEvent;
import club.lyric.infinity.impl.modules.visual.Ambience;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld implements IMinecraft {

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void getCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Vec3d.unpackRgb(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB()));
        }
    }

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    public void addEntityHook(Entity entity, CallbackInfo ci)
    {
        if (ModuleBase.nullCheck()) return;

        RenderEntityEvent.Spawn event = new RenderEntityEvent.Spawn(entity);
        EventBus.getInstance().post(event);

        if (entity instanceof EnderPearlEntity pearl)
        {
            World world = pearl.getWorld();

            PlayerEntity player = world.getClosestPlayer(pearl.getX(), pearl.getY(), pearl.getZ(), 2.0, Entity::isAlive);

            if (player != null)
            {
                PearlEvent pearlEvent = new PearlEvent(pearl, player);
                EventBus.getInstance().post(pearlEvent);
            }
        }
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void removeEntityHook(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci)
    {
        if (ModuleBase.nullCheck()) return;

        RenderEntityEvent.Removal event = new RenderEntityEvent.Removal(mc.world.getEntityById(entityId));

        EventBus.getInstance().post(event);
    }


}