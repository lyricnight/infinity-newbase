package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.manager.client.CommandManager;
import club.lyric.infinity.manager.fabric.EventManager;

import java.util.Arrays;

/**
 * @author lyric
 * for all managers.
 */
public class Managers {
    public static final EventManager EVENTS = new EventManager();

    public static final CommandManager COMMANDS = new CommandManager();

    /**
     * method used to subscribe all managers to the relevant eventbuses.
     */
    public static void subscribe()
    {
        Infinity.LOGGER.info("Starting subscription of managers.");
        subscribe(EVENTS, COMMANDS);
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
        Infinity.LOGGER.info("Subscribing " + Arrays.toString(sub));
        Infinity.EVENT_BUS.subscribe(sub);
    }

}
