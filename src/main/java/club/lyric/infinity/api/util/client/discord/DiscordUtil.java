package club.lyric.infinity.api.util.client.discord;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.RichPresence;
import club.lyric.infinity.manager.Managers;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;

/**
 * @author railhack
 */
public class DiscordUtil implements IMinecraft {
    private static final DiscordRPC lib = DiscordRPC.INSTANCE;
    private static final String applicationId = "1231915223385116723";
    private static final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private static volatile boolean running = false;
    public static synchronized void start() {
        if (!running) {
            handlers.ready = (user) -> Infinity.LOGGER.info("Discord RPC starting!");
            lib.Discord_Initialize(applicationId, handlers, true, "");

            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.details = "";
            presence.largeImageKey = "logo";
            presence.largeImageText = "Infinity for 1.20";
            lib.Discord_UpdatePresence(presence);

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    if (mc.world == null) {
                        presence.details = "Infinity v1.0.2";
                        if (mc.currentScreen instanceof SelectWorldScreen) {
                            presence.state = "Selecting a world.";
                        } else if (mc.currentScreen instanceof TitleScreen) {
                            presence.state = "In the main menu.";
                        } else {
                            presence.state = "Playing the game.";
                        }
                    }

                    //there has to be a better way to do this...
                    if (mc.player != null)
                    {
                        boolean GAME_NAME = Managers.MODULES.getModuleFromClass(RichPresence.class).ign.value();
                        boolean IP_ADD = Managers.MODULES.getModuleFromClass(RichPresence.class).ip.value();

                        if (!GAME_NAME)
                        {
                            if (IP_ADD)
                            {
                                presence.state = "Playing on " + mc.getNetworkHandler().getServerInfo().address;
                            }
                            else
                            {
                                presence.state = "Playing on a server.";
                            }
                        }
                        else
                        {
                            if (IP_ADD)
                            {
                                presence.state = "Playing on " + mc.getNetworkHandler().getServerInfo().address + " as " + mc.player.getDisplayName();
                            }
                            else
                            {
                                presence.state = "Playing on a server as " + mc.player.getDisplayName();
                            }
                        }
                    }
                    try {
                        // noinspection BusyWait
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
                lib.Discord_UpdatePresence(presence);
            }, "RPC-Callback-Handler").start();

            running = true;
        } else {
            throw new RuntimeException("start() called when RPC has already started. Report this!");
        }
    }

    public static synchronized void stop() {
        if (running) {
            lib.Discord_ClearPresence();
            lib.Discord_Shutdown();
            running = false;
        } else {
            throw new RuntimeException("stop() called when RPC has not started. Report this!");
        }
    }
}
