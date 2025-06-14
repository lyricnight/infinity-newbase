package club.lyric.infinity.api.util.client.math;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lyric
 * tried to make this threaded and non-threaded
 * credit to 3arth for showing how this works
 */
public interface StopWatch {
    /**
     * gets a point in time
     *
     * @return - time in long
     */
    long getTimePoint();

    /**
     * sets the point in time we are at
     *
     * @param ms - long in
     */

    void setTimePoint(long ms);

    /**
     * time passed
     *
     * @return - long of time passed since
     */
    default long getPassed() {
        return Time.getPassedTimeSince(getTimePoint());
    }

    /**
     * check if time > input
     *
     * @param ms - input
     * @return - true/false
     */
    default boolean hasBeen(long ms) {
        return Time.isTimePointOlderThan(this.getTimePoint(), ms);
    }

    /**
     * resets time
     */

    default void reset() {
        setTimePoint(Time.getMillis());
    }

    /**
     * when you don't use it in a thread.
     */
    @Getter
    @Setter
    class Single implements StopWatch {
        private long timePoint;

    }

    /**
     * for multiple threads.
     */
    @Setter
    @Getter
    class Multi implements StopWatch {
        private volatile long timePoint;

    }
}
