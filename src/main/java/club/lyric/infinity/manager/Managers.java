package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.manager.client.CommandManager;
import club.lyric.infinity.manager.client.ConfigManager;
import club.lyric.infinity.manager.client.FriendsManager;
import club.lyric.infinity.manager.client.ModuleManager;
import club.lyric.infinity.manager.fabric.EventManager;

/**
 * @author lyric
 * for all managers.
 */
public class Managers {

    public static ConfigManager CONFIG = new ConfigManager();
    public static EventManager EVENTS = new EventManager();
    public static CommandManager COMMANDS = new CommandManager();
    public static FriendsManager FRIENDS = new FriendsManager();
    public static ModuleManager MODULES = new ModuleManager();

    /**
     * subscribing all managers...
     */
    public static void sub()
    {
        Infinity.LOGGER.info("starting manager subscription.");
        subscribe(CONFIG, EVENTS, COMMANDS, FRIENDS, MODULES);
    }


    /**
     * inits managers.
     */
    public static void init()
    {
        Infinity.LOGGER.info("Initialising Managers....");
        MODULES.init();
        CONFIG.init();
        CONFIG.load();
        COMMANDS.init();
        Infinity.LOGGER.info("Manager initialisation complete.");
    }


    /**
     * method used to unload the managers that need it.
     */
    public static void unload()
    {

    }

    /**
     * subscribes all managers
     * @param subscribers the managers to subscribe
     */

    private static void subscribe(Object... subscribers)
    {
        for (Object sub : subscribers)
        {
            Infinity.LOGGER.info("attempting to subscribe " + sub);
            Infinity.EVENT_BUS.subscribe(sub);
        }
        Infinity.LOGGER.info("all managers subscribed.");
    }
}
