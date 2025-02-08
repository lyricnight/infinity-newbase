package club.lyric.infinity;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lyric
 * @since 12/12/23
 * main class.
 */

public final class Infinity implements ModInitializer, ClientModInitializer, IMinecraft {
	public static String CLIENT_NAME = "Infinity";
	public static final String VERSION = "2.0-dev";
    public static final Logger LOGGER = LogManager.getLogger("Infinity");
	private static long start = 0;

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
	}
}