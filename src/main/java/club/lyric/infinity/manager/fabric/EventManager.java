package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.client.SettingEvent;
import club.lyric.infinity.api.event.mc.DeathEvent;
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
import me.bush.eventbus.annotation.EventListener;
import me.bush.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;


/**
 * @author lyric
 * for events
 */

public class EventManager implements IMinecraft {

    /**
     * made this to cache modules, meaning that we don't need to run Managers.MODULES.getModules() everytime an event is fired
     */
    private Set<ModuleBase> moduleCache = new HashSet<>();


    /**
     * only call this after modules have been initialised.
     */
    public void init()
    {
        moduleCache = Managers.MODULES.getModules();
    }


    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.LOWEST)
    public void onSetting(SettingEvent ignored)
    {
        Infinity.LOGGER.info("Tried to refresh moduleCache");
        moduleCache.clear();
        moduleCache = Managers.MODULES.getModules();
    }


    /**
     * for commands.
     * @param event - the chat event
     */
    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.LOWEST)
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(Managers.COMMANDS.getPrefix())) {
            event.cancel();

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

    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onUpdate(UpdateEvent ignored)
    {
        moduleCache.stream().filter(ModuleBase::isEnabled).forEach(ModuleBase::onUpdate);
    }

    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.HIGH)
    public void onWorldRender(Render3DEvent event) {
        moduleCache.stream().filter(ModuleBase::isEnabled).forEach(module -> module.onRender3D(event));
    }

    @SuppressWarnings("unused")
    @EventListener(priority = ListenerPriority.HIGH)
    public void onWorldRender(Render2DEvent event)
    {
        moduleCache.stream().filter(ModuleBase::isEnabled).forEach(module -> module.onRender2D(event));
    }

    //TODO: add this
    public void onTick()
    {
        if (mc.player == null || mc.world == null)
            return;
        //Managers.MODULES.onTick();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            Infinity.EVENT_BUS.post(new DeathEvent(player));
        }
    }
}
