package club.lyric.infinity.api.util.client.chat;

import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.util.client.render.text.StringUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Manager;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * @author vasler, lyric
 * chatuils
 */

public class ChatUtils implements IMinecraft
{
    private static String clientMessage = "";

    public static void format()
    {
        clientMessage = StringUtils.coloredString("[", Managers.MODULES.getModuleFromClass(Manager.class).bracket.getMode()) + StringUtils.coloredString("Infinity", Managers.MODULES.getModuleFromClass(Manager.class).nameColour.getMode()) + StringUtils.coloredString("] ", Managers.MODULES.getModuleFromClass(Manager.class).bracket.getMode());
    }

    public static void sendMessagePrivate(String message)
    {
        format();
        MutableText text = Text.empty();
        text.append(clientMessage);
        text.append(" " + message);
        mc.inGameHud.getChatHud().addMessage(text);
    }

    public static void sendOverwriteMessage(String message, int id)
    {
        if(mc.world == null) return;
        format();
        MutableText text = Text.empty();
        text.append(clientMessage + " ");
        text.append(message);
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }

    public static void sendOverwriteMessageNoTag(String message, int id)
    {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(message);
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }
}