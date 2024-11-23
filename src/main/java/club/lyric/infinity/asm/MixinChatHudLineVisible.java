package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author lyric
 * @see MixinChatHud
 */
@Mixin(ChatHudLine.Visible.class)
public class MixinChatHudLineVisible implements IChatHudLine {
    @Unique
    private int id;
    @Override
    public int infinity$getId() {
        return id;
    }
    @Override
    public void infinity$setId(int id) {
        this.id = id;
    }
}
