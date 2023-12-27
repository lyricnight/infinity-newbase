package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;

import java.lang.management.ManagementFactory;
import java.util.List;

public class AntiClassSave extends AntiDump {

    @Override
    protected void execute() throws Throwable {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

        boolean debugClassLoading = Boolean.parseBoolean(System.getProperty("legacy.debugClassLoading", "false")) || inputArguments.contains("-Dlegacy.debugClassLoading");
        boolean debugClassLoadingFiner = Boolean.parseBoolean(System.getProperty("legacy.debugClassLoadingFiner", "false")) || inputArguments.contains("-Dlegacy.debugClassLoadingFiner");
        boolean debugClassLoadingSave = Boolean.parseBoolean(System.getProperty("legacy.debugClassLoadingSave", "false")) || inputArguments.contains("-Dlegacy.debugClassLoadingSave");

        if (debugClassLoading || debugClassLoadingFiner || debugClassLoadingSave) {
            throw new Throwable("Class debug saving property enabled.");
        }
    }
}
