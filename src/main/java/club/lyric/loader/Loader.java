package club.lyric.loader;

import club.lyric.infinity.Infinity;
import club.lyric.loader.stage.ClientLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import java.lang.reflect.Constructor;

public class Loader implements ModInitializer, ClientModInitializer {

    public static Natives natives = new Natives();

    @Override
    public void onInitialize() {
        /**
        try {
            //new ClientLoader().onInit();
            Class<?> infinityClass = Class.forName("club.lyric.infinity.Infinity");
            Constructor<?> ctor = infinityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            ((Infinity) ctor.newInstance()).onInitialize();
        } catch (Throwable t) {
            throw new RuntimeException();
        }
         */
    }

    @Override
    public void onInitializeClient() {
        /**
        try {
            Class<?> infinityClass = Class.forName("club.lyric.infinity.Infinity");
            Constructor<?> ctor = infinityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            ((Infinity) ctor.newInstance()).onInitializeClient();
        } catch (Throwable t) {
            throw new RuntimeException();
        }
         */
    }
}
