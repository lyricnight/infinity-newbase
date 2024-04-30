package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.impl.events.mc.*;
import club.lyric.infinity.impl.events.mc.update.UpdateEvent;
import club.lyric.infinity.impl.events.render.Render2DEvent;
import club.lyric.infinity.impl.events.render.Render3DEvent;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

/**
 * @author lyric
 * for events
 */

@SuppressWarnings("unused")
public final class EventManager implements IMinecraft {

    /**
     * this needs -1 highest priority for eventbus
     * @param ignored - event
     */

    @EventHandler(priority = Integer.MAX_VALUE - 1)
    public void onUpdate(UpdateEvent ignored)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onUpdate);
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            EventBus.getInstance().post(new DeathEvent(player));
        }
    }

    @EventHandler
    public void onWorldRender(Render3DEvent event)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(module -> module.onRender3D(event));
    }

    @EventHandler
    public void onWorldRender(Render2DEvent event)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(module -> module.onRender2D(event));
    }

    @EventHandler(priority = Integer.MAX_VALUE - 2)
    public void onTick(TickEvent.Pre event)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onTickPre);
    }

    @EventHandler(priority = Integer.MAX_VALUE - 3)
    public void onTickPost(TickEvent.Post event)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onTickPost);
        Managers.TIMER.update();
    }

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
