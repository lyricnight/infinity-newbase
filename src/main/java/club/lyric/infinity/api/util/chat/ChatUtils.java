package club.lyric.infinity.api.util.chat;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.mixininterface.IChatHud;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * @author vasler, lyric
 * chatuils
 */

public class ChatUtils implements IMinecraft
{
    public static void sendMessagePrivate(String message)
    {
        mc.inGameHud.getChatHud().addMessage(Text.of(Managers.COMMANDS.getClientMessage() + " " + message));
    }

    public static void sendOverwriteMessage(String message, int id)
    {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(message);
        ((IChatHud) mc.inGameHud.getChatHud()).addCustom(text, id);
    }
}