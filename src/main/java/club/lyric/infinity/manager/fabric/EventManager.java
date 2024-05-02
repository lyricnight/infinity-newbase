package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * @author lyric
 * for events where I can't keep it in the mixin
 */

@SuppressWarnings("unused")
public final class EventManager implements IMinecraft {

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (event.getAction() == GLFW.GLFW_RELEASE)
            return;

        if (mc.currentScreen instanceof ChatScreen)
            return;

        if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_F3))
            return;

        Managers.MODULES.getModules().stream().filter(m -> m.getBind() == event.getKey()).forEach(ModuleBase::toggle);
    }
}
