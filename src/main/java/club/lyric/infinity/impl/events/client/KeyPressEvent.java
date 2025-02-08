package club.lyric.infinity.impl.events.client;

import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class KeyPressEvent extends Event {
    private final int key;
    private final int action;
}