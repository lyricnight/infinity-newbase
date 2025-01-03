package club.lyric.infinity.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {
    @Accessor(value = "playerKnockback")
    void setPlayerKnockback(Optional<Vec3d> playerKnockback);
}