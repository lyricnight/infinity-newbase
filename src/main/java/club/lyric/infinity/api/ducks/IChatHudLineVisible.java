package club.lyric.infinity.api.ducks;

/**
 * @author lyric
 * @see club.lyric.infinity.asm.MixinChatHudLineVisible
 */
public interface IChatHudLineVisible extends IChatHudLine {
    boolean infinity$isStartOfEntry();

    void infinity$setStartOfEntry(boolean start);
}