package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.entity.Entity;

public class RenderEntityEvent extends Event
{

    public Entity entity;

    public RenderEntityEvent(Entity entity)
    {
        this.entity = entity;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public static class Removal extends RenderEntityEvent
    {
        public Removal(Entity entity)
        {
            super(entity);
        }
    }

    public static class Spawn extends RenderEntityEvent
    {
        public Spawn(Entity entity)
        {
            super(entity);
        }
    }

}