package club.lyric.infinity.impl.events.irc;

import club.lyric.infinity.api.event.Event;

/**
 * @author vasler
 */
public class IRCEvent extends Event {
    private final String nick;
    private final String text;

    public IRCEvent(String nick, String text) {
        this.nick = nick;
        this.text = text;
    }

    public String getNick() {
        return nick;
    }

    public String getText() {
        return text;
    }
}