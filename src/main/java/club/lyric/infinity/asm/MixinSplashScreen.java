package club.lyric.infinity.asm;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SplashOverlay.class)
public class MixinSplashScreen implements IMinecraft {
}
