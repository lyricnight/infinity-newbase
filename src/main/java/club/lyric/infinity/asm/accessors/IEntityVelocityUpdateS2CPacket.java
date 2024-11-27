package club.lyric.infinity.asm.accessors;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author lyric
 */
@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface IEntityVelocityUpdateS2CPacket {
    @Mutable
    @Accessor(value = "velocityX")
    void setVelocityX(int velocityX);

    @Mutable
    @Accessor(value = "velocityY")
    void setVelocityY(int velocityY);

    @Mutable
    @Accessor(value = "velocityZ")
    void setVelocityZ(int velocityZ);
}