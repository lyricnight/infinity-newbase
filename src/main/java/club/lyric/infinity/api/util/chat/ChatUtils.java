package club.lyric.infinity.api.util.chat;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.text.Text;

/**
 * @author vasler
 * chatuils
 */

public class ChatUtils implements IMinecraft
{
    //VALSER DONT CHANGE THIS YOU MONKEY
    public static final String CLIENT_MESSAGE = "[Infinity]";

    public static void sendMessagePrivate(String message)
    {
        mc.inGameHud.getChatHud().addMessage(Text.of(CLIENT_MESSAGE + " " + message));
    }

    public static void sendOverwriteMessage(String message, int id)
    {
        //for id messages
    }

}