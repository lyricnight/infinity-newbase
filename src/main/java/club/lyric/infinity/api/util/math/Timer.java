package club.lyric.infinity.api.util.math;

public class Timer {
    private volatile long time;

    public boolean passed(double ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    public boolean passed(long ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

    public boolean every(long ms) {
        boolean passed = getMs(System.nanoTime() - time) >= ms;
        if (passed)
            reset();
        return passed;
    }

    public long getMs() {
        return System.currentTimeMillis() - time;
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public void set(long ns) {
        time = ns;
    }

}