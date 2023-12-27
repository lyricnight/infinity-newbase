package club.lyric.loader;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Natives {

    public interface DLL extends Library {
        DLL INSTANCE = Native.loadLibrary("/Infinity.dll", DLL.class);

        String native0();

        String native1();

        String native2();

        String native3();

        String native4();

        void native5();

        void native6();

        void native7(String jsonString, boolean shutdown);
    }

    public String native0() {
        return DLL.INSTANCE.native0();
    }

    public String native1() {
        return DLL.INSTANCE.native1();
    }

    public String native2() {
        return DLL.INSTANCE.native2();
    }

    public String native3() {
        return DLL.INSTANCE.native3();
    }

    public String native4() {
        return DLL.INSTANCE.native4();
    }

    public void native5() {
        DLL.INSTANCE.native5();
    }

    public void native6() {
        DLL.INSTANCE.native6();
    }

    public void native7(String jsonString, boolean shutdown) {
        DLL.INSTANCE.native7(jsonString, shutdown);
    }

    private static void copyFile(final InputStream input, final OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

    private static FileOutputStream openOutputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    public void loadDll() throws Throwable {
        Path tempDirectoryPath = Files.createTempDirectory("dll");
        File f = new File(tempDirectoryPath + File.separator + "Infinity.dll");

        try (InputStream in = Natives.class.getResourceAsStream("/Infinity.dll"); OutputStream out = openOutputStream(f)) {
            copyFile(in, out);
            tempDirectoryPath.toFile().deleteOnExit();
            f.deleteOnExit();
        }

        System.load(f.getAbsolutePath());
    }
}
