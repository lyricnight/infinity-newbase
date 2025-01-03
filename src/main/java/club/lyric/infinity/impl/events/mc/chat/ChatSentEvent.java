package club.lyric.infinity.impl.events.mc.chat;


import club.lyric.infinity.api.event.Event;
import lombok.Getter;

/**
 * @author lyric
 * event fired whenever a chat message is sent
 */

@Getter
public class ChatSentEvent extends Event {
    private final String message;

    public ChatSentEvent(String message) {
        this.message = message;
    }

}
