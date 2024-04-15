package club.lyric.infinity.asm.accessors;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface IEntityVelocityUpdateS2CPacket {

    @Accessor(value = "velocityX")
    void setVelocityX(int velocityX);

    @Accessor(value = "velocityY")
    void setVelocityY(int velocityY);

    @Accessor(value = "velocityZ")
    void setVelocityZ(int velocityZ);
}