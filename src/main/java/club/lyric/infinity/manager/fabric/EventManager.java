package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.mc.DeathEvent;
import club.lyric.infinity.api.event.mc.update.UpdateEvent;
import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.mc.ChatEvent;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;


/**
 * @author lyric
 * for events
 */

public class EventManager implements IMinecraft {
    /**
     * for commands.
     * @param event - the chat event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(Infinity.COMMANDS.getPrefix())) {
            event.cancel();

            String[] arguments = event.getMessage().replaceFirst(Infinity.COMMANDS.getPrefix(), "").split(" ");

            boolean isCommand = false;

            for (Command commands : Infinity.COMMANDS.getCommands()) {
                if (commands.getCommand().equals(arguments[0])) {
                    commands.onCommand(arguments);

                    isCommand = true;

                    break;
                }
            }
            if (!isCommand) {
                ChatUtils.sendMessagePrivate(Formatting.RED + "Unknown command. Try " + Infinity.COMMANDS.getPrefix() + "commands for a list of available commands.");
            }
        }
    }

    /**
     * @param event - allows event to work my nigger
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUpdate(UpdateEvent event)
    {
        Infinity.MODULES.onUpdate();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldRender(Render3DEvent event) {
        Infinity.MODULES.onRender3D(event);
    }

    public void onTick()
    {
        if (mc.player == null || mc.world == null)
            return;
        Infinity.MODULES.onTick();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            Infinity.EVENT_BUS.post(new DeathEvent(player));
        }
    }

    /**
     * @param event - allows event to work my nigger
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayEvent(Render2DEvent event) {
        Infinity.MODULES.onRender2D(event);
    }

}
