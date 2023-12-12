package club.lyric.infinity.api.event;

/**
 * @author lyric
 * basic event class.
 */

public class Event {

    private boolean cancelled = false;

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void cancel()
    {
        cancelled = true;
    }
}
