package club.lyric.infinity.api.util.client;

import club.lyric.infinity.Infinity;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordUtil {
    private static final DiscordRPC lib = DiscordRPC.INSTANCE;
    private static final String applicationId = "1087455760092500130";
    private static final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private static volatile boolean running = false;
    public static synchronized void start() {
        if (!running) {
            handlers.ready = (user) -> Infinity.LOGGER.info("Discord RPC starting!");
            lib.Discord_Initialize(applicationId, handlers, true, "");

            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.details = "Testing RPC";
            presence.largeImageKey = "image";
            presence.largeImageText = "Valser made me set the image to this";
            lib.Discord_UpdatePresence(presence);

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        //noinspection BusyWait,BlockingMethodInNonBlockingContext
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            }, "RPC-Callback-Handler").start();

            running = true;
        } else {
            System.out.println("Discord RPC is already running.");
        }
    }

    public static synchronized void stop() {
        if (running) {
            lib.Discord_ClearPresence();
            lib.Discord_Shutdown();
            running = false;
        } else {
            System.out.println("Discord RPC is not running.");
        }
    }
}
