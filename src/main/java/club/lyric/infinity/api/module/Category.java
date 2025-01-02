package club.lyric.infinity.api.module;

/**
 * @author valser
 * categories
 */

public enum Category {
    COMBAT,
    EXPLOIT,
    PLAYER,
    MISC,
    MOVEMENT,
    VISUAL,
    CLIENT;

    public final String getName
            = name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
}
