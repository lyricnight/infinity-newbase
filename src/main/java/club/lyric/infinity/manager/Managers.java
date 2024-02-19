package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.manager.client.*;
import club.lyric.infinity.manager.fabric.EventManager;
import club.lyric.infinity.manager.fabric.ServerManager;
import club.lyric.infinity.manager.fabric.TimerManager;

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
    public static TimerManager TIMER = new TimerManager();

    /**
     * subscribing all managers...
     */
    public static void sub()
    {
        Infinity.LOGGER.info("starting manager subscription.");
        subscribe(FRIENDS, CONFIG, EVENTS, ROTATIONS, COMMANDS, SERVER, MODULES, TEXT, TIMER);
    }


    /**
     * initialises managers.
     */
    public static void init()
    {
        Infinity.LOGGER.info("Initialising Managers....");
        MODULES.init();
        CONFIG.loadConfig();
        COMMANDS.init();
        Infinity.LOGGER.info("Manager initialisation complete.");
    }

    /**
     * subscribes managers
     * @param subscribers the managers to subscribe
     */

    private static void subscribe(Object... subscribers)
    {
        for (Object sub : subscribers)
        {
            Infinity.LOGGER.info("attempting to subscribe " + sub);
            EventBus.getInstance().register(sub);
        }
        Infinity.LOGGER.info("all managers subscribed.");
    }
}
