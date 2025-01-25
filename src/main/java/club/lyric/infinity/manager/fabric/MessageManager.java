package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.ducks.IChatHud;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * @author lyric
 * This manager handles all messages sent to chat by the client.
 * I made this a manager, since its called constantly.
 */
public final class MessageManager implements IMinecraft {

    /**
     * Returns the client prefix
     */
    private Text clientPrefix()
    {
        MutableText clientMessage = Text.literal("[" + "Infinity" + "]");
        return clientMessage.setStyle(clientMessage.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
    }

    /**
     * sends a message to chat with the client prefix
     * @param message - the message to send
     * @param colored - if the message should be colored
     */
    public void sendMessage(String message, boolean colored) {
        if (Null.is()) return;
        MutableText text = Text.empty().append(clientPrefix()).append(" " + message);
        if (colored) {
            text.setStyle(text.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
        }
        mc.inGameHud.getChatHud().addMessage(text);
    }

    /**
     * sends a message to chat with the client prefix, but can be overwritten.
     * @param message - the message to send
     * @param id - the id of the message as an int - used for overwriting
     * @param colored - if the message should be colored
     */
    public void sendOverwriteMessage(String message, int id, boolean colored)
    {
        if (Null.is()) return;
        MutableText text = Text.empty().append(clientPrefix()).append(" " + message);
        if (colored) {
            text.setStyle(text.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
        }
        ((IChatHud) mc.inGameHud.getChatHud()).infinity$add(text, id);
    }
}
