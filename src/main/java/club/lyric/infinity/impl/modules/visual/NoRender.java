package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.events.render.RenderParticleEvent;
import net.minecraft.client.particle.ExplosionLargeParticle;

public class NoRender extends ModuleBase {


    public NoRender() {
        super("NoRender", "CCC", Category.Visual);
    }

    @EventHandler
    public void onParticleRender(RenderParticleEvent event) {
        if (event.getParticle() instanceof ExplosionLargeParticle) {
            event.setCancelled(true);
        }
    }
}
