package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager implements IClientPlayerInteractionManager {
    @Override
    @Accessor(value = "blockBreakingCooldown")
    public abstract void setHitDelay(int delay);
}
