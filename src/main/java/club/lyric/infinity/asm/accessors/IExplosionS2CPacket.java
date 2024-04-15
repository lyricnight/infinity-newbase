package club.lyric.infinity.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {

    @Accessor(value = "playerVelocityX")
    void setPlayerVelocityX(float playerVelocityX);

    @Accessor(value = "playerVelocityY")
    void setPlayerVelocityY(float playerVelocityY);

    @Accessor(value = "playerVelocityZ")
    void setPlayerVelocityZ(float playerVelocityZ);
}