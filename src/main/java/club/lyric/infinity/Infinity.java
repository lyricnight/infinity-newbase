package club.lyric.infinity;

import club.lyric.infinity.manager.Managers;
import club.lyric.loader.Loader;
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

public class Infinity implements ModInitializer, ClientModInitializer {

	private Infinity() {
		//Loader.natives.native5();
	}

	public static final String CLIENT_NAME = "Infinity";
	public static final String VERSION = " v2";
    public static final Logger LOGGER = LogManager.getLogger("Infinity");

	@Override
	public void onInitialize() {
        LOGGER.log(Level.INFO, "  _        __ _       _ _         ");
		LOGGER.log(Level.INFO, " (_)      / _(_)     (_) |        ");
		LOGGER.log(Level.INFO, "  _ _ __ | |_ _ _ __  _| |_ _   _ ");
		LOGGER.log(Level.INFO, " | | '_ \\|  _| | '_ \\| | __| | | |");
		LOGGER.log(Level.INFO, " | | | | | | | | | | | | |_| |_| |");
		LOGGER.log(Level.INFO, " |_|_| |_|_| |_|_| |_|_|\\__|\\__, |");
		LOGGER.log(Level.INFO, "                             __/ |");
		LOGGER.log(Level.INFO, "                            |___/ ");
		LOGGER.info("Infinity has received onInitialize()!");
		Managers.sub();
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.init();
		Runtime.getRuntime().addShutdownHook(new Thread(Managers.CONFIG::save));
	}
}