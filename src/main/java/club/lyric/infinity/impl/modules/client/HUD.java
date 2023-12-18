package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import org.lwjgl.glfw.GLFW;

public class HUD extends ModuleBase {
    public HUD() {
        super("HUD", "Displays HUD elements on the screen.", Category.CLIENT);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        event.getDrawContext().drawText(mc.textRenderer, Infinity.CLIENT_NAME, 2, 2, -1, true);
    }
}
