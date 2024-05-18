package club.lyric.infinity.api.util.client.combat;

import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author lyric
 */
public class TargetUtils implements IMinecraft {
    public static PlayerEntity getTarget(double range)
    {
        if (mc.player == null || mc.world == null) return null;
        return mc.world.getPlayers().stream().filter(Objects::nonNull).filter(LivingEntity::isAlive).filter(entity -> entity != mc.player).filter(entity -> entity.getId() != mc.player.getId()).filter(entity -> !Managers.FRIENDS.isFriend(entity)).filter(entity -> mc.player.squaredDistanceTo(entity) <= MathUtils.square(range)).min(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity))).orElse(null);
    }
}
