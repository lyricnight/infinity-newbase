package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;
import sun.misc.Unsafe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class AntiNetworkDebuggers extends AntiDump {

    // this is just a lazy one but it still works

    @Override
    protected void execute() throws Throwable {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = processBuilder.command("tasklist.exe").start();
        BufferedReader resultReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = resultReader.readLine()) != null) {
            if ((line = line.toLowerCase()).contains("wireshark") || line.contains("httpdebugger") || line.contains("smartsniff") || line.contains("everything")) {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                ((Unsafe) theUnsafeField.get(null)).freeMemory(System.class.hashCode());
                Runtime.getRuntime().halt(0);
            }
        }
    }
}
