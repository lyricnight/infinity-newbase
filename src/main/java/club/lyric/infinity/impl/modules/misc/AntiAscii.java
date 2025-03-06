package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.util.Formatting;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author vasler
 */

@SuppressWarnings("unused")
public final class AntiAscii extends ModuleBase {
    public final NumberSetting offset = new NumberSetting("Offset", this, 15f, 1.0f, 256.0f, 1f);
    private static final ThreadLocal<CharsetEncoder> asciiEncoder = ThreadLocal.withInitial(StandardCharsets.US_ASCII::newEncoder);

    public AntiAscii() {
        super("AntiAscii", "Removes ascii from chat to prevent freezes.", Category.MISC);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ChatMessageS2CPacket) {
            String character = ((ChatMessageS2CPacket) event.getPacket()).body().content();
            int counter = 0;
            for (char characters : character.toCharArray()) {
                if (!asciiEncoder.get().canEncode(characters)) {
                    counter++;
                }
            }

            if (counter >= offset.getIValue()) {
                event.setCancelled(true);
                Managers.MESSAGES.sendOverwriteMessage(Formatting.RED + "Blocked a chat message with " + counter + " flags.", 2222, true);
            }
        }
    }

    @Override
    public String moduleInformation() {
        return offset.getFValue() + "";
    }
}
