package club.lyric.infinity.impl.events.irc;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;

/**
 * @author vasler
 */
@Getter
public class IRCEvent extends Event {
    private final String nick;
    private final String text;

    public IRCEvent(String nick, String text) {
        this.nick = nick;
        this.text = text;
    }

}