package club.lyric.infinity.asm;

import club.lyric.infinity.impl.modules.exploit.KickPrevent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * @author lyric
 * remember to bump this when version upgrade
 */
@Mixin(PacketByteBuf.class)
public abstract class MixinPacketByteBuf {
    @ModifyArg(method = "readNbt(Lnet/minecraft/nbt/NbtSizeTracker;)Lnet/minecraft/nbt/NbtElement;", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/DataInput;Lnet/minecraft/nbt/NbtSizeTracker;)Lnet/minecraft/nbt/NbtElement;"))
    private NbtSizeTracker xlPackets(NbtSizeTracker sizeTracker) {
        return Managers.MODULES.getModuleFromClass(KickPrevent.class).isOn() ? NbtSizeTracker.ofUnlimitedBytes() : sizeTracker;
    }
}