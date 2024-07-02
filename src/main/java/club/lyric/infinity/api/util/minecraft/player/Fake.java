package club.lyric.infinity.api.util.minecraft.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author 3arth
 */
public class Fake extends AbstractClientPlayerEntity {
    public static final GameProfile RANDOM_GAMEPROFILE = new GameProfile(UUID.randomUUID(), "FakePlayer");
    public static final PlayerListEntry DUMMY_PLAYERINFO = new PlayerListEntry(RANDOM_GAMEPROFILE, true);

    public Fake(ClientWorld clientWorld) {
        super(clientWorld, RANDOM_GAMEPROFILE);
    }

    public Fake(ClientWorld clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    protected PlayerListEntry getPlayerListEntry() {
        return DUMMY_PLAYERINFO;
    }

    @Override
    protected @NotNull MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    /**
     * prevents pushing
     */
    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {
    }
}
