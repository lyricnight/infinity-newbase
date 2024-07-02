package club.lyric.infinity.impl.events.mc.chat;


import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 * event fired whenever a chat message is sent
 */

public class ChatSentEvent extends Event {
    private final String message;

    public ChatSentEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
