package club.lyric.infinity.impl.events.mc.chat;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public class ReceiveChatEvent extends Event {

    private Text message;
    private MessageIndicator indicator;

    private boolean added;

    public int id;

    public ReceiveChatEvent(Text text, MessageIndicator messageIndicator, int id) {
        this.message = text;
        this.id = id;
        this.indicator = messageIndicator;
    }

    public Text getMessage() {
        return message;
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }

    public void setMessage(Text message) {
        this.message = message;
        this.added = true;
    }

    public void setIndicator(MessageIndicator indicator) {
        this.indicator = indicator;
        this.added = true;
    }

    public boolean hasBeenAdded() {
        return added;
    }
}
