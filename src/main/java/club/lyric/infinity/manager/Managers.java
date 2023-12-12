package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.manager.fabric.EventManager;

import java.util.Arrays;

/**
 * @author lyric
 * for all managers.
 */
public class Managers {
    public static final EventManager EVENTS = new EventManager();

    /**
     * method used to subscribe all managers to the relevant eventbuses.
     */
    public static void subscribe()
    {
        Infinity.LOGGER.info("Starting subscription of managers.");
    }


    /**
     * method used to unload the managers that need it.
     */
    public static void unload()
    {

    }


    /**
     * subscribes a manager.
     * @param manager - the manager to subscribe
     */

    private void subscribe(Object ... manager)
    {
        Infinity.LOGGER.info("Subscribing " + Arrays.toString(manager));
        Infinity.EVENT_BUS.subscribe(manager);
    }

}
