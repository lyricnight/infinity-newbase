package club.lyric.infinity.api.event.mc;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author lyric
 * basic totempop thing
 */
public class TotemPopEvent extends Event {

    private PlayerEntity playerEntity;

    private int id;

    public TotemPopEvent(PlayerEntity playerEntity, int id)
    {
        this.playerEntity = playerEntity;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }
}
