package club.lyric.infinity.impl.events.irc;

import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vasler
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IRCEvent extends Event {
    private final String nick;
    private final String text;
}