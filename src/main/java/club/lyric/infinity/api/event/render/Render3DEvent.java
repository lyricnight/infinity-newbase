package club.lyric.infinity.api.event.render;


import me.lyric.eventbus.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    private final MatrixStack matrixStack;

    public Render3DEvent(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public MatrixStack getMatrix() {
        return matrixStack;
    }

    @Override
    protected boolean isCancellable() {
        return true;
    }
}