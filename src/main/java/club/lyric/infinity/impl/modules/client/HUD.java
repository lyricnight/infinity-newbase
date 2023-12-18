package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.chat.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

public class HUD extends ModuleBase {
    public HUD() {
        super("HUD", "Displays HUD elements on the screen.", Category.CLIENT);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @EventHandler
    public void hi(PacketEvent event)
    {
        ChatUtils.sendMessagePrivate("HI");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        event.getDrawContext().drawText(mc.textRenderer, Infinity.CLIENT_NAME, 2, 2, -1, true);
    }
}
