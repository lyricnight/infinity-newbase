package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.render.text.StringUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lyric
 */

@SuppressWarnings("unused")
public class Chat extends ModuleBase {
    public BooleanSetting clear =
            new BooleanSetting(
            "Clear",
            true,
            this
    );

    public BooleanSetting remove =
            new BooleanSetting(
            "RemoveLine",
            true,
            this
    );

    public BooleanSetting infiniteMessages =
            new BooleanSetting(
            "InfiniteMessages",
            true,
            this
    );

    public BooleanSetting timeStamps =
            new BooleanSetting(
            "TimeStamps",
            false,
            this
    );

    public ModeSetting time = new ModeSetting("TimeColour", this, "DarkGray", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");

    public ModeSetting brackets = new ModeSetting("BracketColour", this, "Black", "None", "Black", "DarkGray", "Gray", "DarkBlue", "Blue", "DarkGreen", "Green", "DarkAqua", "Aqua", "DarkRed", "Red", "DarkPurple", "Purple", "Gold", "Yellow");


    public BooleanSetting keep = new BooleanSetting("Keep", false, this);

    // for timestamps
    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm");

    public final IntList lines = new IntArrayList();

    public Chat()
    {
        super("Chat", "Handles our chat and how it looks...", Category.Render);
    }


    //no point getting/setting message if timestamps aren't on
    @EventHandler(priority = 800)
    public void onChatReceive(ReceiveChatEvent event)
    {
        if (timeStamps.value()) {
            Text message = event.getMessage();
            MutableText bracketPre = Text.literal("<").formatted(StringUtils.getCodeFromSetting(brackets.getMode()));
            MutableText dateText = Text.literal(date.format(new Date())).formatted(StringUtils.getCodeFromSetting(time.getMode()));
            MutableText bracketPost = Text.literal("> ").formatted(StringUtils.getCodeFromSetting(brackets.getMode()));
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
            if (clear.value())
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
