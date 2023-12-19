package club.lyric.infinity.mixininterface;

import com.mojang.authlib.GameProfile;

/**
 * @author lyric
 *
 */
public interface IChatHudLine {
    String getOurText();
    int getOurId();
    void setOurId(int id);
    GameProfile getOurSender();
    void setOurSender(GameProfile profile);
}
