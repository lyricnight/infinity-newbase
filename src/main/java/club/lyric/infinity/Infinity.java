package club.lyric.infinity;

import club.lyric.infinity.manager.Managers;
import club.lyric.infinity.manager.client.CommandManager;
import club.lyric.infinity.manager.client.ConfigManager;
import club.lyric.infinity.manager.client.FriendsManager;
import club.lyric.infinity.manager.client.ModuleManager;
import club.lyric.infinity.manager.fabric.EventManager;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

/**
 * @author lyric
 * @since 12/12/23
 * main class.
 */

public class Infinity implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("infinity");
	public static final IEventBus EVENT_BUS = new EventBus();

	// managers //
	public static ConfigManager CONFIG = new ConfigManager();
	public static EventManager EVENTS = new EventManager();
	public static CommandManager COMMANDS = new CommandManager();
	public static FriendsManager FRIENDS = new FriendsManager();
	public static ModuleManager MODULES = new ModuleManager();


	@Override
	public void onInitialize() {
		LOGGER.info("Infinity has received onInitialize()!");
		LOGGER.info("Initialising eventbus.");
		EVENT_BUS.registerLambdaFactory("club.lyric.infinity", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
		LOGGER.info("LambdaFactory has been initialised...");
		EVENT_BUS.subscribe(EVENTS);
		EVENT_BUS.subscribe(CONFIG);
		EVENT_BUS.subscribe(COMMANDS);
		EVENT_BUS.subscribe(FRIENDS);
		EVENT_BUS.subscribe(MODULES);
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
		Managers.init();
		Runtime.getRuntime().addShutdownHook(new Thread(Infinity.CONFIG::save));
	}
}