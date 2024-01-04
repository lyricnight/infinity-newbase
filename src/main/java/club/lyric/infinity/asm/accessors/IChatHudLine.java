package club.lyric.infinity.asm.accessors;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author lyric
 * @see club.lyric.infinity.asm.MixinChatHud
 */
@Mixin(ChatHud.class)
public interface IChatHudLine {
    @Accessor("visibleMessages")
    List<ChatHudLine.Visible> getVisibleMessages();

    @Accessor("messages")
    List<ChatHudLine> getMessages();
}


