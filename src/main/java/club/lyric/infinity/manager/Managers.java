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
    public static final EventManager EVENTS = new EventManager();
    public static final ConfigManager CONFIG = new ConfigManager();
    public static final CommandManager COMMANDS = new CommandManager();
    public static final FriendsManager FRIENDS = new FriendsManager();
    public static final ModuleManager MODULES = new ModuleManager();

    /**
     * method used to subscribe all managers to the relevant eventbuses.
     */
    public static void subscribe()
    {
        Infinity.LOGGER.info("Starting subscription of managers.");
        subscribe(EVENTS, COMMANDS, FRIENDS, CONFIG, MODULES);
        Infinity.LOGGER.info("Finished subscription of managers.");
    }

    public static void init()
    {
        Infinity.LOGGER.info("Initialising Managers....");
        MODULES.init();
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
     * subscribes a manager.
     * @param sub - the manager to subscribe
     */

    private static void subscribe(Object... sub)
    {
        for (Object subscriber : sub)
        {
            Infinity.LOGGER.info("Subscribing " + subscriber);
            Infinity.EVENT_BUS.subscribe(sub);
        }
    }

}
