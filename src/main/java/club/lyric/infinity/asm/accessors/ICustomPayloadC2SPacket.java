package club.lyric.infinity.asm.accessors;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author lyric
 * for modhider
 */
@Mixin(CustomPayloadC2SPacket.class)
public interface ICustomPayloadC2SPacket {
    /**@Accessor(value = "write")
    void setData(PacketByteBuf buffer);
    @Accessor(value = "channel")
    Identifier getChannel();*/
}
