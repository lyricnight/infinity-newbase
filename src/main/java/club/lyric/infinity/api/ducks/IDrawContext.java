package club.lyric.infinity.api.ducks;

import net.minecraft.client.font.TextRenderer;
import org.jetbrains.annotations.Nullable;

/**
 * @author lyric
 * @see club.lyric.infinity.manager.client.TextManager
 */
public interface IDrawContext {
    float infinity_newbase$drawText(TextRenderer renderer, @Nullable String text, float x, float y, int color, boolean shadow);
}
