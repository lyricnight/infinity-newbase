package club.lyric.infinity.impl.events.mc.item;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * lowkey just recoded sexmasters one
 */
public class PearlEvent extends Event
{
    private final PlayerEntity player;
    private final EnderPearlEntity pearl;

    public PearlEvent(EnderPearlEntity pearl, PlayerEntity player)
    {
        this.player = player;
        this.pearl = pearl;
    }

    public PlayerEntity getPlayer()
    {
        return player;
    }

    public EnderPearlEntity getPearl()
    {
        return pearl;
    }
}