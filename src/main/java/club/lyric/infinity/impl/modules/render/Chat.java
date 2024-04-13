package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.ReceiveChatEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
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

@SuppressWarnings("unused")
public final class Chat extends ModuleBase {
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
            message = Text.empty().append(timeStamps()).append(message);
            event.setMessage(message);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Text timeStamps()
    {
        MutableText timeStamps = Text.literal("<" + date.format(new Date()) + "> " + Formatting.RESET);
        return timeStamps.setStyle(timeStamps.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
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
