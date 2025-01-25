package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.discord.DiscordEventHandlers;
import club.lyric.infinity.api.util.client.discord.DiscordRPC;
import club.lyric.infinity.api.util.client.discord.DiscordRichPresence;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 */
public final class RichPresence extends ModuleBase {
    public RichPresence() {
        super("RichPresence", "Toggles the Discord Presence", Category.CLIENT);
    }
    public BooleanSetting ip = new BooleanSetting("ShowIP", false, this);
    public BooleanSetting ign = new BooleanSetting("ShowIGN", false, this);
    private static DiscordRPC rpc;
    public static DiscordRichPresence presence = new DiscordRichPresence();
    private boolean started = false;
    private static Thread thread;

    @Override
    public void onEnable() {
        if (MinecraftClient.IS_SYSTEM_MAC)
        {
            Managers.MESSAGES.sendMessage(Formatting.RED + "You cannot enable DiscordRPC on a Mac-based system due to ARM dll incompatibility.", false);
            disable();
            return;
        }
        rpc = DiscordRPC.INSTANCE;
        start();
    }

    @Override
    public void onDisable() {
        if (MinecraftClient.IS_SYSTEM_MAC)
        {
            return;
        }
        started = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    private void start() {
        if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1231915223385116723", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = "Infinity " + Infinity.VERSION + " for 1.21.1.";
            presence.largeImageKey = "real";
            rpc.Discord_UpdatePresence(presence);

            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();

                    presence.details = getStatus();

                    presence.state = "Testing modules.";

                    rpc.Discord_UpdatePresence(presence);
                    try {
                        //noinspection BusyWait
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "Infinity-RPC");
            thread.start();
        }
    }

    private String getStatus() {
        if (mc.currentScreen instanceof TitleScreen) {
            return "In the main menu.";
        }
        if (mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof AddServerScreen) {
            return "Choosing a server.";
        }
        if (mc.getCurrentServerEntry() != null) {
            if (ip.value()) {
                if (ign.value()) {
                    return "Playing on " + mc.getCurrentServerEntry().address + " as " + mc.player.getDisplayName().getString();
                } else {
                    return "Playing on " + mc.getCurrentServerEntry().address;
                }
            } else {
                if (ign.value()) {
                    return "Playing as " + mc.player.getDisplayName().getString();
                } else {
                    return "Playing on a server.";
                }
            }
        }
        if (mc.isInSingleplayer()) {
            return "Playing singleplayer.";
        }
        return "Loading the game.";
    }
}

