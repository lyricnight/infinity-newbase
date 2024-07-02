package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.PersistentModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.events.render.RenderEntityEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

/**
 * @author lyric & vasler
 */
public class Notifications extends PersistentModuleBase {
    public BooleanSetting totemPops =
            new BooleanSetting(
                    "TotemPops",
                    false,
                    this
            );

    public BooleanSetting toggled =
            new BooleanSetting(
                    "Toggled",
                    true,
                    this
            );

    public BooleanSetting render =
            new BooleanSetting(
                    "Render",
                    false,
                    this
            );

    public Notifications() {
        super("Notifications", "Notifies in chat for stuff.", Category.Client);
    }

    @EventHandler
    public void onRemovalEntity(RenderEntityEvent.Removal event) {

        if (nullCheck()) return;

        if (event.getEntity() instanceof PlayerEntity player && render.value()) {

            if (player.getName().getString().equalsIgnoreCase(mc.player.getName().getString())) return;

            ChatUtils.sendOverwriteMessageColored(Managers.OTHER.getAppropriateFormatting(player) + player.getDisplayName().getString() + Formatting.RESET + " has left your render.", player.getId());
        }
    }

    @EventHandler
    public void onRemovalEntity(RenderEntityEvent.Spawn event) {

        if (nullCheck()) return;

        if (event.getEntity() instanceof PlayerEntity player && render.value()) {

            if (player.getName().getString().equalsIgnoreCase(mc.player.getName().getString())) return;

            ChatUtils.sendOverwriteMessageColored(Managers.OTHER.getAppropriateFormatting(player) + player.getDisplayName().getString() + Formatting.RESET + " has entered your render.", player.getId());
        }
    }
}
