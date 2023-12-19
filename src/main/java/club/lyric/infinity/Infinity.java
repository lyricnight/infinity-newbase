package club.lyric.infinity;

import club.lyric.infinity.manager.Managers;
import me.lyric.eventbus.bus.EventBus;
import me.lyric.eventbus.handler.handlers.LambdaHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lyric
 * @since 12/12/23
 * main class.
 */

public class Infinity implements ModInitializer, ClientModInitializer {
	public static final String CLIENT_NAME = "Infinity";
	public static final String VERSION = " v2";
    public static final Logger LOGGER = LogManager.getLogger("Infinity");
	public static EventBus EVENT_BUS = new EventBus(LambdaHandler.class, LOGGER::error, LOGGER::info);
	@Override
	public void onInitialize() {
		LOGGER.info("Infinity has received onInitialize()!");
		Managers.sub();
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.init();
		Runtime.getRuntime().addShutdownHook(new Thread(Managers.CONFIG::save));
		EVENT_BUS.getInfo();
	}
}