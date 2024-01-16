package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.client.SettingEvent;
import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.util.client.render.animations.Easings;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lyric
 */

@SuppressWarnings({"unused", "unchecked"})
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
            false,
            "Renders timestamps before all messages."
    ));

    public EnumSetting<Formatting> time = createEnum(new EnumSetting<>("TimeColour", Formatting.DARK_GRAY, v -> timeStamps.getValue(), "Colour of the time in timeStamps."));

    public EnumSetting<Formatting> brackets = createEnum(new EnumSetting<>("BracketColour", Formatting.BLACK, v -> timeStamps.getValue(), "Colour of the brackets in timeStamps."));


    public BooleanSetting keep = createBool(
            new BooleanSetting(
            "Keep",
            false,
            "Keeps chat messages when you disconnect."
    ));


    // for timestamps
    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm");

    public final IntList lines = new IntArrayList();

    public Chat()
    {
        super("Chat", "Handles our chat and how it looks...", Category.RENDER);
    }

    @EventHandler(priority = 500)
    public void onSetting(SettingEvent event)
    {
        clear();
    }




    //no point getting/setting message if timestamps aren't on
    @EventHandler(priority = 800)
    public void onChatReceive(ReceiveChatEvent event)
    {
        if (timeStamps.getValue()) {
            Text message = event.getMessage();
            MutableText bracketPre = Text.literal("<").formatted(brackets.getValue());
            MutableText dateText = Text.literal(date.format(new Date())).formatted(time.getValue());
            MutableText bracketPost = Text.literal("> ").formatted(brackets.getValue());
            Text timestamp = Text.literal(bracketPre + String.valueOf(dateText) + bracketPost);
            message = Text.empty().append(timestamp).append(message);
            event.setMessage(message);
        }
    }

    /**
     * gets rid of background
     */
    public void clear()
    {
        if (mc.options != null)
        {
            if (clear.getValue())
            {
                mc.options.getTextBackgroundOpacity().setValue(0.0);
            }
            else
            {
                mc.options.getTextBackgroundOpacity().setValue(0.5);
            }
        }
    }

    @Override
    public void onDisable()
    {
        if (mc.options.getTextBackgroundOpacity().getValue() == 0.0)
        {
            mc.options.getTextBackgroundOpacity().setValue(0.5);
        }
    }
}
