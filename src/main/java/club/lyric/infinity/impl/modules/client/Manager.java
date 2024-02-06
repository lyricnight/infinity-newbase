package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.asm.accessors.ICustomPayloadC2SPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

/**
 * @author lyric
 */
public final class Manager extends ModuleBase {
    public ModeSetting bracket = new ModeSetting("Bracket", this, "Black", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public ModeSetting nameColour = new ModeSetting("NameColour", this, "DarkGray", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public BooleanSetting modHider = new BooleanSetting("ModHider", false, this);

    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.Client);
    }

    @EventHandler(priority = 500)
    public void onPacketSend(PacketEvent.Send event)
    {
        if (event.getPacket() instanceof CustomPayloadC2SPacket customPayloadC2SPacket && modHider.value())
        {
            ICustomPayloadC2SPacket packet = (ICustomPayloadC2SPacket) customPayloadC2SPacket;
            Identifier identifier = packet.getChannel();
            if (identifier.equals(CustomPayloadC2SPacket.BRAND))
            {
                packet.setData(new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
            }
        }
    }
}
