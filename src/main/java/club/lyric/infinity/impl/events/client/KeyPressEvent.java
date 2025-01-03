package club.lyric.infinity.impl.events.client;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;

@Getter
public class KeyPressEvent extends Event {
    private final int key;
    private final int action;

    public KeyPressEvent(int key, int action) {
        this.key = key;
        this.action = action;
    }

}