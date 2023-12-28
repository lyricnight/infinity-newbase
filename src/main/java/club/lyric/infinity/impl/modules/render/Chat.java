package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lyric
 */

//TODO: Fix this
public class Chat extends ModuleBase {
    public BooleanSetting clear = createBool(
            new BooleanSetting(
            "Clear",
            true,
            "Makes chat clear."
    ));

    public BooleanSetting remove = createBool(
            new BooleanSetting(
            "RemoveLine",
            true,
            "Removes the line on the side of chat messages."
    ));

    public BooleanSetting infiniteMessages = createBool(
            new BooleanSetting(
            "InfiniteMessages",
            true,
            "Allows you to type infinitely long messages."
    ));

    public BooleanSetting timeStamps = createBool(
            new BooleanSetting(
            "TimeStamps",
            true,
            "Renders timestamps before all messages."
    ));

    public BooleanSetting keep = createBool(
            new BooleanSetting(
            "Keep",
            true,
            "Keeps chat messages when you disconnect."
    ));


    // for timestamps
    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm");

    public final IntList lines = new IntArrayList();

    public Chat()
    {
        super("Chat", "Handles our chat and how it looks...", Category.RENDER);
    }

    @EventHandler
    public void onChatReceive(ReceiveChatEvent event)
    {
        Text message = event.getMessage();
        if (timeStamps.getValue()) {
            //TODO: add custom colors, will be done in a module (something like Internals in old infinity, or i could just add the enum for it here?)
            Text timestamp = Text.literal("<" + date.format(new Date()) + "> ").formatted(Formatting.LIGHT_PURPLE);

            message = Text.empty().append(timestamp).append(message);
        }

        event.setMessage(message);
    }
}
