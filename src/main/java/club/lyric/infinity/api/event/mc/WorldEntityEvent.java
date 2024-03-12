package club.lyric.infinity.api.event.mc;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.Entity;

/**
 * @author lyric
 */
public class WorldEntityEvent extends Event {

    private Entity entity;

    public WorldEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public static class Remove extends WorldEntityEvent
    {
        public Remove(Entity entity)
        {
            super(entity);
        }
    }

    public static class Add extends WorldEntityEvent
    {
        public Add(Entity entity)
        {
            super(entity);
        }
    }
}
