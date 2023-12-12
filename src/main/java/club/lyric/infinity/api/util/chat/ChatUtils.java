package club.lyric.infinity.api.util.chat;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.text.Text;

/**
 * @author vasler
 * chatuils
 */

public class ChatUtils implements IMinecraft
{
    public static void sendMessagePrivate(String message)
    {
        mc.inGameHud.getChatHud().addMessage(Text.literal(message));
    }

}