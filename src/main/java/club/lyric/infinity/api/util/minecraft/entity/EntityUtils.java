package club.lyric.infinity.api.util.minecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;

public class EntityUtils {

    public static boolean isMob(Entity entity)
    {
        return entity instanceof Monster;
    }

    public static boolean isAnimal(Entity entity)
    {
        return entity instanceof AnimalEntity || entity instanceof PassiveEntity;
    }

}
