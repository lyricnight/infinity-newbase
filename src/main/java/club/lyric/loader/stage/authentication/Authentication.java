package club.lyric.loader.stage.authentication;

import club.lyric.loader.Loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Authentication {

    private final static List<String> modList = new ArrayList<>();

    public static void authenticate() throws Throwable {
        collectModNames();
        copyDll2();
    }

    private static void collectModNames() throws Throwable {
        Files.walk(Paths.get(System.getenv("APPDATA") + File.separatorChar + ".minecraft" + File.separatorChar + "mods"), 1).filter(
                it -> !it.toFile().isDirectory() && it.toFile().getName().endsWith(".jar")
        ).forEach(jar -> modList.add(jar.toFile().getName()));
    }

    private static void copyDll2() throws Throwable {
        URLConnection connection = new URL(Loader.natives.native0()).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty(Loader.natives.native3(), Loader.natives.native4());
        Files.createDirectories(Paths.get(Loader.natives.native1()));
        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); FileOutputStream fos = new FileOutputStream(Loader.natives.native1() + Loader.natives.native2())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
            }
        }
        Loader.natives.native6();
    }

    public static List<String> getModList() {
        return modList;
    }
}
