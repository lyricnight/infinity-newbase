package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.client.SettingEvent;
import club.lyric.infinity.api.event.mc.DeathEvent;
import club.lyric.infinity.api.event.mc.TickEvent;
import club.lyric.infinity.api.event.mc.update.UpdateEvent;
import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.mc.ChatEvent;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import me.lyric.eventbus.annotation.EventListener;
import me.lyric.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * for events
 */

@SuppressWarnings("unused")
public class EventManager implements IMinecraft {
    /**
     * for commands.
     * @param event - the chat event
     */
    @EventListener(priority = ListenerPriority.LOWEST)
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(Managers.COMMANDS.getPrefix())) {
            event.setCancelled(true);

            String[] arguments = event.getMessage().replaceFirst(Managers.COMMANDS.getPrefix(), "").split(" ");

            boolean isCommand = false;

            for (Command commands : Managers.COMMANDS.getCommands()) {
                if (commands.getCommand().equals(arguments[0])) {
                    commands.onCommand(arguments);

                    isCommand = true;

                    break;
                }
            }
            if (!isCommand) {
                ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown command. Try " + Managers.COMMANDS.getPrefix() + "commands for a list of available commands.");
            }
        }
    }

    /**
     * this needs highest priority for eventbus
     * @param ignored - event
     */

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onUpdate(UpdateEvent ignored)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onUpdate);
    }

    @EventListener(priority = ListenerPriority.LOW)
    public void onWorldRender(Render3DEvent event) {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(module -> module.onRender3D(event));
    }

    @EventListener(priority = ListenerPriority.LOW)
    public void onWorldRender(Render2DEvent event)
    {
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(module -> module.onRender2D(event));
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onTick(TickEvent event)
    {
        if (mc.player == null || mc.world == null) return;
        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(ModuleBase::onTick);
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            Infinity.EVENT_BUS.post(new DeathEvent(player));
        }
    }
}
