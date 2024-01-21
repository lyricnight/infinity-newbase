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

@SuppressWarnings({"unused"})
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

    public EnumSetting<Formatting> time = new EnumSetting<>("TimeColour", this, Formatting.DARK_GRAY);

    public EnumSetting<Formatting> brackets = new EnumSetting<>("BracketColour", this, Formatting.BLACK);


    public BooleanSetting keep = new BooleanSetting("Keep", false, this);

    // for timestamps
    private final SimpleDateFormat date = new SimpleDateFormat("HH:mm");

    public final IntList lines = new IntArrayList();

    public Chat()
    {
        super("Chat", "Handles our chat and how it looks...", Category.Render);
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
        if (timeStamps.value()) {
            Text message = event.getMessage();
            MutableText bracketPre = Text.literal("<").formatted(brackets.getMode());
            MutableText dateText = Text.literal(date.format(new Date())).formatted(time.getMode());
            MutableText bracketPost = Text.literal("> ").formatted(brackets.getMode());
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
