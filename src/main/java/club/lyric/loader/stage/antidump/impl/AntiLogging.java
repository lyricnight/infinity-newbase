package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class AntiLogging extends AntiDump {
    @Override
    protected void execute() throws Throwable {
        OutputStream dummyStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        System.setOut(new PrintStream(dummyStream));
    }
}
