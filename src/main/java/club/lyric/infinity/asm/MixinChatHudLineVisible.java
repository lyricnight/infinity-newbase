package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IChatHudLineVisible;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author lyric
 * @see MixinChatHud
 */
@Mixin(ChatHudLine.Visible.class)
public class MixinChatHudLineVisible implements IChatHudLineVisible {
    @Shadow
    @Final
    private OrderedText content;
    @Unique
    private int id;
    @Unique
    private GameProfile sender;
    @Unique
    private boolean startOfEntry;

    @Override
    public String infinity$getText() {
        StringBuilder stringBuilder = new StringBuilder();
        content.accept((index, style, codePoint) -> {
            stringBuilder.appendCodePoint(codePoint);
            return true;
        });

        return stringBuilder.toString();
    }

    @Override
    public int infinity$getId() {
        return id;
    }

    @Override
    public void infinity$setId(int id) {
        this.id = id;
    }

    @Override
    public GameProfile infinity$getSender() {
        return sender;
    }

    @Override
    public void infinity$setSender(GameProfile profile) {
        sender = profile;
    }

    @Override
    public boolean infinity$isStartOfEntry() {
        return startOfEntry;
    }

    @Override
    public void infinity$setStartOfEntry(boolean start) {
        startOfEntry = start;
    }
}
