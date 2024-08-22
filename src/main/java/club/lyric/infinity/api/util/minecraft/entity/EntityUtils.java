package club.lyric.infinity.api.util.minecraft.entity;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author vasler and lyric
 */
public class EntityUtils implements IMinecraft {

    public static boolean isMob(Entity entity) {
        return entity instanceof Monster;
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof PassiveEntity;
    }

    /**
     * function to get blockPos of player
     * @return above
     */
    public static BlockPos getPlayerPos() {
        return Math.abs(mc.player.getVelocity().getY()) > 0.1 ? BlockPos.ofFloored(mc.player.getPos()) : getPosition(mc.player);
    }

    private static BlockPos getPosition(Entity entity) {
        double y = entity.getY();
        if (entity.getY() - Math.floor(entity.getY()) > 0.5) {
            y = Math.ceil(entity.getY());
        }

        return BlockPos.ofFloored(entity.getX(), y, entity.getZ());
    }

    public static boolean inRenderDistance(double posX, double posZ)
    {
        double x = Math.abs(mc.gameRenderer.getCamera().getPos().x - posX);
        double z = Math.abs(mc.gameRenderer.getCamera().getPos().z - posZ);

        double d = (mc.options.getViewDistance().getValue() + 1) * 16;

        return x < d && z < d;
    }
}
