package club.lyric.infinity;

import club.lyric.infinity.manager.Managers;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author lyric
 * @since 12/12/23
 * main class.
 */

public class Infinity implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("infinity");

	public static final IEventBus EVENT_BUS = new EventBus();

	@Override
	public void onInitialize() {
		LOGGER.info("Infinity has received onInitialize()!");
		LOGGER.info("Initialising eventbus.");
		EVENT_BUS.registerLambdaFactory("infinity", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
		LOGGER.info("LambdaFactory has been initialised...");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.subscribe();
		//shutdownhook goes here to save config on shutdown
		//Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
	}
}