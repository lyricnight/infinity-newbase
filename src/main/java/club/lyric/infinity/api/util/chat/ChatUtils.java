package club.lyric.infinity.api.util.chat;

import club.lyric.infinity.api.util.client.StringUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * @author vasler
 * chatuils
 */

public class ChatUtils implements IMinecraft
{
    public static final String CLIENT_MESSAGE = StringUtils.coloredString("[Infinity]");

    public static void sendMessagePrivate(String message)
    {
        //you need to add infinity prefix to this
        mc.inGameHud.getChatHud().addMessage(Text.literal(message));
    }
    public static void sendOverwriteMessage(String message, int id)
    {
        //for id messages
    }
}