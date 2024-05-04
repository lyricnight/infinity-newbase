package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.visual.Chat;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * @author lyric
 * @see club.lyric.infinity.impl.modules.visual.Chat
 */
@Mixin(StringHelper.class)
public class MixinStringHelper {
    @ModifyArg(method = "truncateChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringHelper;truncate(Ljava/lang/String;IZ)Ljava/lang/String;"), index = 1)
    private static int injected(int maxLength) {
        return (Managers.MODULES.getModuleFromClass(Chat.class).infiniteMessages.value() ? Integer.MAX_VALUE : maxLength);
    }
}