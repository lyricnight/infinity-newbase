package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.client.spotify.Spotify;
import club.lyric.infinity.manager.client.*;
import club.lyric.infinity.manager.fabric.*;

/**
 * @author lyric
 * for all managers.
 */
public final class Managers {

    public static ModuleManager MODULES = new ModuleManager();
    public static FriendsManager FRIENDS = new FriendsManager();
    public static ConfigManager CONFIG = new ConfigManager();
    public static EventManager EVENTS = new EventManager();
    public static RotationManager ROTATIONS = new RotationManager();
    public static CommandManager COMMANDS = new CommandManager();
    public static ServerManager SERVER = new ServerManager();
    public static TextManager TEXT = new TextManager();
    public static AntiCheatManager ANTICHEAT = new AntiCheatManager();
    public static TimerManager TIMER = new TimerManager();
    public static OtherEntityManager OTHER = new OtherEntityManager();
    public static InventoryManager INVENTORY = new InventoryManager();

    /**
     * subscribing all managers...
     */
    public static void sub()
    {
        Infinity.LOGGER.info("starting manager subscription.");
        subscribe(FRIENDS, CONFIG, EVENTS, ROTATIONS, COMMANDS, SERVER, MODULES, TEXT, TIMER, ANTICHEAT);
    }


    /**
     * initialises managers.
     */
    public static void init()
    {
        Infinity.LOGGER.info("starting manager init.");
        MODULES.init();
        CONFIG.loadConfig();
        COMMANDS.init();
        Infinity.LOGGER.info("Manager initialisation complete.");
    }

    /**
     * unloads all managers that need it.
     */
    public static void unload()
    {
        CONFIG.saveConfig();
        OTHER.unload();
        TIMER.unload();
    }

    /**
     * subscribes managers
     * @param subscribers the managers to subscribe
     */

    private static void subscribe(Object... subscribers)
    {
        for (Object sub : subscribers)
        {
            Infinity.LOGGER.info("attempting to subscribe {}", sub);
            EventBus.getInstance().register(sub);
        }
        Infinity.LOGGER.info("all managers subscribed.");
    }
}
