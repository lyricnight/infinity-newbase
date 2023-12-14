package club.lyric.infinity.manager;

import club.lyric.infinity.Infinity;

/**
 * @author lyric
 * for all managers.
 */
public class Managers {
    public static void init()
    {
        Infinity.LOGGER.info("Initialising Managers....");
        Infinity.MODULES.init();
        Infinity.CONFIG.init();
        Infinity.CONFIG.load();
        Infinity.COMMANDS.init();
        Infinity.LOGGER.info("Manager initialisation complete.");
    }


    /**
     * method used to unload the managers that need it.
     */
    public static void unload()
    {

    }
}
