package club.lyric.infinity.api.util.client.chat;

import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * @author vasler, lyric
 * chatuils
 */

@SuppressWarnings("ConstantConditions")
public class ChatUtils implements IMinecraft
{

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

    /**
     * ID 1111 = Caution
     * ID 2222 = Danger
     * ID 3333 = Perfect
     */
    public static void sendOverwriteMessage(String message, int id)
    {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(clientMessage());
        text.append(" " + message);
        if (id == 1111)
        {
            text.append(" (!)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        else if (id == 2222)
        {
            text.append(" (-)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        else if (id == 3333)
        {
            text.append(" (*)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }

    public static void sendOverwriteMessageNoTag(String message, int id)
    {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(message);
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }

    public static void sendOverwriteMessageColored(String message, int id) {
        if(mc.world == null) return;
        MutableText text = Text.empty();
        text.append(clientMessage());
        text.append(" " + message);
        text.setStyle(text.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
        if (id == 1111)
        {
            text.append(" (!)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        else if (id == 2222)
        {
            text.append(" (-)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        else if (id == 3333)
        {
            text.append(" (*)");
            ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
            return;
        }
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }
}