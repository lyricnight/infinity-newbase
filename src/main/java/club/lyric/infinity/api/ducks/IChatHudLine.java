package club.lyric.infinity.api.ducks;

import com.mojang.authlib.GameProfile;

public interface IChatHudLine {
    String infinity$getText();
    int infinity$getId();
    void infinity$setId(int id);
    GameProfile infinity$getSender();
    void infinity$setSender(GameProfile profile);
}
