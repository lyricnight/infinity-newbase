package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;

import java.lang.management.ManagementFactory;

public class LaunchArgsChecker extends AntiDump {

    @Override
    protected void execute() throws Throwable {
        String[] disallowedArgs = {
                "-XBootclasspath",
                "-javaagent",
                "-Xdebug",
                "-agentlib",
                "-Xrunjdwp",
                "-Xnoagent",
                "-verbose",
                "-Xverify:none",
                "-DproxySet",
                "-DproxyHost",
                "-DproxyPort",
                "-Djavax.net.ssl.trustStore",
                "-Djavax.net.ssl.trustStorePassword"
        };
        for (String arg : disallowedArgs) {
            for (String inArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
                if (inArg.contains(arg)) {
                    throw new Throwable("Strange launch arg: " + arg);
                }
            }
        }
    }
}
