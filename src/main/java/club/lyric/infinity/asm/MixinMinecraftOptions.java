package club.lyric.infinity.asm;

import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * @author lyric
 */
@SuppressWarnings("SameReturnValue")
@Mixin(GameOptions.class)
public class MixinMinecraftOptions {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 110))
    private int maxFovValueHook(int constant) {
        return 180;
    }
}
