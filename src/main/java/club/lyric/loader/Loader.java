package club.lyric.loader;

import club.lyric.infinity.Infinity;
import club.lyric.loader.stage.ClientLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import java.lang.reflect.Constructor;

public class Loader {

    public static Natives natives = new Natives();
    /*
    @Override
    public void onInitialize() {
        try {
            Class<?> infinityClass = Class.forName("club.lyric.infinity.Infinity");
            Constructor<?> ctor = infinityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            ((Infinity) ctor.newInstance()).onInitialize();
            new ClientLoader().onInit();
        } catch (Throwable t) {
            Infinity.LOGGER.error("Error: ", t);
            t.printStackTrace();
        }
    }

    @Override
    public void onInitializeClient() {
        try {
            Class<?> infinityClass = Class.forName("club.lyric.infinity.Infinity");
            Constructor<?> ctor = infinityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            ((Infinity) ctor.newInstance()).onInitializeClient();
        } catch (Throwable t) {
            Infinity.LOGGER.error("Error: ", t);
            t.printStackTrace();
        }
    }
    */
}
