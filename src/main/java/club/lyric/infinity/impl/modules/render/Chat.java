package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.lyric.eventbus.annotation.EventListener;
import me.lyric.eventbus.annotation.ListenerPriority;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lyric
 */

//TODO: Fix this
public class Chat extends ModuleBase {
    public BooleanSetting clear = new BooleanSetting("Clear", true, "Makes chat clear.");

    public BooleanSetting remove = new BooleanSetting("RemoveLine", true, "Removes the line on the side of chat messages.");

    public BooleanSetting infiniteMessages = new BooleanSetting("InfiniteMessages", true, "Allows you to type infinitely long messages.");

    public BooleanSetting timeStamps = new BooleanSetting("TimeStamps", true, "Renders timestamps before all messages.");

    public BooleanSetting keep = new BooleanSetting("Keep", true, "Keeps chat messages when you disconnect.");


    // for timestamps
    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm");

    public final IntList lines = new IntArrayList();

    public Chat()
    {
        super("Chat", "Handles our chat and how it looks...", Category.RENDER);
        instantiate(this, clear, remove, infiniteMessages, timeStamps, keep);
    }


    @EventListener(priority = ListenerPriority.LOW)
    public void onChatReceive(ReceiveChatEvent event)
    {
        Text message = event.getMessage();
        if (timeStamps.getValue()) {
            //TODO: add custom colors
            Text timestamp = Text.literal("<" + date.format(new Date()) + "> ").formatted(Formatting.LIGHT_PURPLE);

            message = Text.empty().append(timestamp).append(message);
        }

        event.setMessage(message);
    }
}
