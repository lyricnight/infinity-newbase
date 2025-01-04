package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.network.AbstractClientPlayerEntity;

/**
 * @author lyric
 * fired whenever our player model is drawn.
 */
@Getter @Setter
public final class RenderPlayerModelEvent extends Event {
    private final AbstractClientPlayerEntity entity;
    private float yaw;
    private float pitch;

    public RenderPlayerModelEvent(AbstractClientPlayerEntity entity) {
        this.entity = entity;
    }
}
