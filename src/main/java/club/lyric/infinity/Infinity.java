package club.lyric.infinity;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lyric
 * @since 12/12/23
 * main class.
 */

public final class Infinity implements ModInitializer, ClientModInitializer, IMinecraft {
	public static String CLIENT_NAME = "Infinity";
	public static final String VERSION = Version.NIGHTLY ? " v" + Version.VERSION + "-nightly" : " v" + Version.VERSION;
    public static final Logger LOGGER = LogManager.getLogger("Infinity");
	private static long start;

	@Override
	public void onInitialize() {
		start = System.nanoTime() / 1000000L;
		LOGGER.info("Infinity has received onInitialize()!");
		Managers.sub();
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.init();
        Infinity.LOGGER.info("Infinity has fully initialised in {} ms.", System.nanoTime() / 1000000L - start);
		LOGGER.log(Level.INFO, "  _        __ _       _ _         ");
		LOGGER.log(Level.INFO, " (_)      / _(_)     (_) |        ");
		LOGGER.log(Level.INFO, "  _ _ __ | |_ _ _ __  _| |_ _   _ ");
		LOGGER.log(Level.INFO, " | | '_ \\|  _| | '_ \\| | __| | | |");
		LOGGER.log(Level.INFO, " | | | | | | | | | | | | |_| |_| |");
		LOGGER.log(Level.INFO, " |_|_| |_|_| |_|_| |_|_|\\__|\\__, |");
		LOGGER.log(Level.INFO, "                             __/ |");
		LOGGER.log(Level.INFO, "                            |___/ ");
		Runtime.getRuntime().addShutdownHook(new Thread(Managers.CONFIG::saveConfig));
	}
}