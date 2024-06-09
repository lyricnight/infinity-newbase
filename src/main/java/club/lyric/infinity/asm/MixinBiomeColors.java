package club.lyric.infinity.asm;


import club.lyric.infinity.impl.modules.visual.Ambience;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.ColorResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class MixinBiomeColors {


    /**@Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private static void getColor(BlockRenderView world, BlockPos pos, ColorResolver resolver, CallbackInfoReturnable<Integer> cir)
    {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB());
        }
    }
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {

            cir.setReturnValue(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB());

        }
    }

    @Inject(method = "getGrassColor", at = @At("HEAD"), cancellable = true)
    private static void getGrassBlock(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB());
        }
    }

    @Inject(method = "getFoliageColor", at = @At("HEAD"), cancellable = true)
    private static void getFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Managers.MODULES.getModuleFromClass(Ambience.class).isOn())
        {
            cir.setReturnValue(Managers.MODULES.getModuleFromClass(Ambience.class).color.getColor().getRGB());
        }
    }*/
}