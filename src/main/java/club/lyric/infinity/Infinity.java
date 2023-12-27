package club.lyric.infinity;

import club.lyric.infinity.manager.Managers;
import club.lyric.loader.Loader;
import me.lyric.eventbus.bus.EventBus;
import me.lyric.eventbus.handler.handlers.LambdaHandler;
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

public class Infinity {

	private Infinity() {
		Loader.natives.native5();
	}

	public static final String CLIENT_NAME = "Infinity";
	public static final String VERSION = " v2";
    public static final Logger LOGGER = LogManager.getLogger("Infinity");
	public static EventBus EVENT_BUS = new EventBus(LambdaHandler.class, LOGGER::error, LOGGER::info);

	public void onInitialize() {
		// mio does it like this and it looks good so dont fix it
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

	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.init();
		Runtime.getRuntime().addShutdownHook(new Thread(Managers.CONFIG::save));
		EVENT_BUS.getInfo();
	}
}