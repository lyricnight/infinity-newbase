package club.lyric.infinity.impl.events.render;

import club.lyric.infinity.api.event.Event;
import net.minecraft.client.util.math.MatrixStack;


public class RenderWorldEvent extends Event {
    private final MatrixStack matrices;
    private final float tickDelta;

    public RenderWorldEvent(MatrixStack matrices, float tickDelta) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrix() {
        return matrices;
    }

    public static class Game extends RenderWorldEvent {

        /**
         * @param matrices - matrix stack
         * @param tickDelta - time taken between rendering
         */
        public Game(MatrixStack matrices, float tickDelta) {
            super(matrices, tickDelta);
        }
    }
}