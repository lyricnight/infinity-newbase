package club.lyric.infinity.api.event;

/**
 * @author lyric
 */
public final class Event {
    /**
     * represents whether an event is cancelled or not.
     */
    private boolean cancelled;

    /**
     * function used to return the value of cancelled.
     * @return - boolean
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * function used to set the value of cancelled
     * @param cancelled - the true/false if the event is cancelled or not.
     */

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}