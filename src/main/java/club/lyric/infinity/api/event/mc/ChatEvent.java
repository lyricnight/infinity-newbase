package club.lyric.infinity.api.event.mc;


import me.lyric.eventbus.event.Event;

/**
 * @author lyric
 * event fired whenever a chat message is sent
 */

public class ChatEvent extends Event {
    private final String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected boolean isCancellable() {
        return true;
    }
}
