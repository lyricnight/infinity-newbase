package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.render.FullBright;
import club.lyric.infinity.manager.Managers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer implements IMinecraft {

    @ModifyReturnValue(method = "getSkyLight", at = @At("RETURN"))
    private int onGetSkyLight(int original) {
        return Math.max(Managers.MODULES.getModuleFromClass(FullBright.class).getLuminance(LightType.SKY), original);
    }

    @ModifyReturnValue(method = "getBlockLight", at = @At("RETURN"))
    private int onGetBlockLight(int original) {
        return Math.max(Managers.MODULES.getModuleFromClass(FullBright.class).getLuminance(LightType.BLOCK), original);
    }

}
