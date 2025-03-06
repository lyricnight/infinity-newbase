package club.lyric.infinity.asm;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;

/**
 * for DFU.
 */
@Mixin(SharedConstants.class)
public class MixinSharedConstants {

    /**
     * @author Andrew Steinborn - LazyDFU developer
     * @reason Disables any possibility of enabling DFU "optimizations"
     * @see <a href="https://github.com/astei/lazydfu/blob/master/src/main/java/me/steinborn/lazydfu/mixin/SharedConstantsMixin.java">...</a>
     */

//    @SuppressWarnings("EmptyMethod")
//    @Overwrite
//    public static void enableDataFixerOptimization() {
//        // Turn this method into a no-op.
//    }
}
