package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.network.AbstractClientPlayerEntity;

/**
 * @author lyric
 * fired whenever our player model is drawn.
 */
public class RenderPlayerModelEvent extends Event {
    private final AbstractClientPlayerEntity entity;
    private float yaw;
    private float pitch;
    public RenderPlayerModelEvent(AbstractClientPlayerEntity entity) {
        this.entity = entity;
    }

    /**
     * getters
     */
    public AbstractClientPlayerEntity getEntity() {
        return entity;
    }

    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }

    /**
     * setters
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
