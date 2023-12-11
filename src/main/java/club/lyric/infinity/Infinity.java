package club.lyric.infinity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Infinity implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("infinity");

	@Override
	public void onInitialize() {
		LOGGER.info("Infinity has received onInitialize()!");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Infinity has received onInitializeClient()!");
	}
}