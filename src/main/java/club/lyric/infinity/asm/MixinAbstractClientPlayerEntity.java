package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.visual.FovModifier;
import club.lyric.infinity.manager.Managers;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity extends PlayerEntity implements IMinecraft {

    public MixinAbstractClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getFovMultiplier", at = @At("HEAD"), cancellable = true)
    public float getFovMultiplier(CallbackInfoReturnable<Float> info)
    {
        FovModifier fovModifier = Managers.MODULES.getModuleFromClass(FovModifier.class);

        if (!fovModifier.isOn()) return 0.0f;

        float f = 1.0F;

        if (getAbilities().flying) {
            f *= fovModifier.flying.getFValue();
        }

        if (mc.player.getStatusEffects().equals(StatusEffects.SPEED))
        {
            f *= fovModifier.speed.getFValue();
        }

        if (mc.player.isSprinting())
        {
            f *= fovModifier.sprinting.getFValue();
        }

        if (getAbilities().getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        ItemStack itemStack = this.getActiveItem();
        if (isUsingItem()) {
            if (itemStack.isOf(Items.BOW)) {
                f *= fovModifier.aiming.getFValue();
            }
        }

        if (fovModifier.staticFov.value())
        {
            return 1.0f;
        }
        return MathHelper.lerp((mc.options.getFovEffectScale().getValue()).floatValue(), 1.0F, f);
    }
}