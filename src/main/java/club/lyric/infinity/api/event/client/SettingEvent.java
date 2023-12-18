package club.lyric.infinity.api.event.client;

import me.bush.eventbus.event.Event;

/**
 * @author lyric
 * event fired on setting change
 */

public class SettingEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return true;
    }
}
