package club.lyric.infinity.api.event.mc;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 * event fired whenever a chat message is sent
 */

public class ChatEvent extends Event {
    private final String message;

    public ChatEvent(String message) {
        this.message = message;
        Infinity.LOGGER.info("ChatEvent fired!");
    }

    public String getMessage() {
        return message;
    }
}
