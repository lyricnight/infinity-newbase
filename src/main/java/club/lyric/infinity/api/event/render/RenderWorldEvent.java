package club.lyric.infinity.api.event.render;

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
         * @param matrices
         * @param tickDelta
         */
        public Game(MatrixStack matrices, float tickDelta) {
            super(matrices, tickDelta);
        }
    }
}