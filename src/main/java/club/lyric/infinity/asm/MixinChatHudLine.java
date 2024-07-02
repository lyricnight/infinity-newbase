package club.lyric.infinity.asm;

import club.lyric.infinity.api.ducks.IChatHudLine;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author lyric
 * @see MixinChatHud
 */
@Mixin(value = ChatHudLine.class)
public class MixinChatHudLine implements IChatHudLine {
    @Shadow
    @Final
    private Text content;
    @Unique
    private int id;
    @Unique
    private GameProfile sender;

    @Override
    public String infinity$getText() {
        return content.getString();
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
}