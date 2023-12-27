package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;

import java.lang.reflect.Field;
import java.util.Vector;

public class ResourcePresenceChecker extends AntiDump {

    @Override
    @SuppressWarnings({ "unchecked" })
    protected void execute() throws Throwable {
        crash_block:{
            try {
                Class.forName("club.lyric.infinity.Infinity");
            } catch (Throwable t) {
                break crash_block;
            }

            throw new Throwable("Infinity client class present too early");
        }

        Field libraries = ClassLoader.class.getDeclaredField("libraries");
        libraries.setAccessible(true);
        Vector<String> loadedLibraries = (Vector<String>) libraries.get(null);

        for (String library : loadedLibraries) {
            if (library.contains("Infinity.dll")) {
                return;
            }
        }

        throw new Throwable("Infinity DLL not loaded");
    }
}