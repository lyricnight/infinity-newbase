package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IClientPlayerInteractionManager;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.impl.events.mc.mine.MineBlockEvent;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager implements IClientPlayerInteractionManager, IMinecraft {
    @Override
    @Accessor(value = "blockBreakingCooldown")
    public abstract void setHitDelay(int delay);

    @Inject(method = "attackBlock", at = @At(value = "HEAD"))
    private void sendAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = mc.world.getBlockState(pos);
        MineBlockEvent attackBlockEvent = new MineBlockEvent(pos, state, direction);
        EventBus.getInstance().post(attackBlockEvent);
        if (attackBlockEvent.isCancelled()) {
            cir.cancel();
        }
    }
}
