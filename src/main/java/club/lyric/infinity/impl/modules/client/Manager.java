package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.asm.accessors.ICustomPayloadC2SPacket;
import club.lyric.infinity.manager.Managers;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.awt.*;

/**
 * @author lyric
 */
public final class Manager extends PersistentModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(Color.BLUE), false);
    public ModeSetting bracket = new ModeSetting("Bracket", this, "Black", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public ModeSetting nameColour = new ModeSetting("NameColour", this, "DarkGray", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");
    public BooleanSetting modHider = new BooleanSetting("ModHider", false, this);

    public Manager()
    {
        super("Manager", "Manages some global settings.", Category.Client);
    }

    /**@EventHandler(priority = 500)
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
    }*/


    public static Color getColor()
    {
        return Managers.MODULES.getModuleFromClass(Manager.class).color.getColor();
    }
}
