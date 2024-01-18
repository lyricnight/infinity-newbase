package club.lyric.infinity.api.ducks;

import net.minecraft.client.font.TextRenderer;
import org.jetbrains.annotations.Nullable;

public interface IDrawContext {
    float infinity_newbase$drawText(TextRenderer renderer, @Nullable String text, float x, float y, int color, boolean shadow);
}
