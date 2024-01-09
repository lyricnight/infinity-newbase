package club.lyric.infinity.api.util.client.enums;

/**
 * @author lyric
 * @see club.lyric.infinity.impl.modules.player.PhaseWalk
 */

public enum PhaseWalkEnum {
    Standard(1337.0D),

    Low(777.0D),

    Zero(0.0D),

    Negative(-666.0D);

    private final double position;

    PhaseWalkEnum(double position)
    {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
