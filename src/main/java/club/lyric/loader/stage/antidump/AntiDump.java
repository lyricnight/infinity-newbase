package club.lyric.loader.stage.antidump;

import club.lyric.loader.stage.antidump.impl.*;

import java.util.Arrays;
import java.util.List;

public abstract class AntiDump {

    protected abstract void execute() throws Throwable;

    public static void runChecks() throws Throwable {
        List<AntiDump> checks = Arrays.asList(
                new ResourcePresenceChecker(),
                new AntiLogging(),
                new LaunchArgsChecker(),
                new AntiNetworkDebuggers(),
                new AntiSunDump(),
                new InstrumentationDisabler(),
                new AntiClassSave()
        );
        for (AntiDump check : checks) {
            check.execute();
        }
    }
}
