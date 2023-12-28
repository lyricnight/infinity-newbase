package club.lyric.infinity.api.event.bus;

/**
 * @author lyric
 * simple priority class.
 */
public enum Priority {
    HIGHEST(9999),
    HIGH(5000),
    NORMAL(2500),
    LOW(500),
    LOWEST(10);

    private final int value;

    private Priority(int value)
    {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
