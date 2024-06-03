package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    private final MatrixStack matrices;
    private DrawContext context;

    public Render3DEvent(MatrixStack matrices) {
        this.matrices = matrices;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public DrawContext getContext() {
        return context;
    }
}
