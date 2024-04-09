package club.lyric.infinity.api.util.client.chat;

import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * @author vasler, lyric
 * chatuils
 */

public class ChatUtils implements IMinecraft
{

    @SuppressWarnings("ConstantConditions")
    public static Text clientMessage()
    {
        MutableText clientMessage = Text.literal("[" + "Infinity" +  "]");
        return clientMessage.setStyle(clientMessage.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
    }

    public static void sendMessagePrivate(String message)
    {
        if (mc.world == null) return;
        MutableText text = Text.empty();
        text.append(clientMessage());
        text.append(" " + message);
        mc.inGameHud.getChatHud().addMessage(text);
    }

    public static void sendOverwriteMessage(String message, int id)
    {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(clientMessage());
        text.append(" " + message);
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