package club.lyric.infinity.api.event.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.gui.DrawContext;

public class Render2DEvent extends Event {
    private final DrawContext drawContext;

    public Render2DEvent(DrawContext drawContext) {
        this.drawContext = drawContext;
    }

    public DrawContext getDrawContext() {
        return drawContext;
    }

}