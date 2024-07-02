package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.particle.Particle;

public class RenderParticleEvent extends Event {

    public Particle particle;

    public RenderParticleEvent(Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }
}
