package club.lyric.infinity.impl.events.mc.chat;

import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lyric
 * event fired whenever a chat message is sent
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ChatSentEvent extends Event {
    private final String message;
}
