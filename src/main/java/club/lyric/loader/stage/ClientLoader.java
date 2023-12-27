package club.lyric.loader.stage;

import club.lyric.infinity.Infinity;
import club.lyric.loader.Loader;
import club.lyric.loader.stage.antidump.AntiDump;
import club.lyric.loader.stage.authentication.Authentication;
import club.lyric.loader.utils.AlertUtils;
import club.lyric.loader.utils.WebhookUtil;
import com.google.gson.JsonObject;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class ClientLoader {

    public static LoadingStage stage = LoadingStage.NATIVES;

    public void onInit() {
        long startTime = System.currentTimeMillis();
        while (stage != LoadingStage.POSTLOAD) {
            try {
                stage.run();
            } catch (Throwable t) {
                handleError(stage, t);
            }
            stage.step();
        }

        Infinity.LOGGER.info(String.format("Finished loading in %ss.", (System.currentTimeMillis() - startTime) / 1000.0D));
    }

    private static void handleError(LoadingStage stage, Throwable t) {
        switch (stage) {
            case AUTHENTICATION -> {
                Infinity.LOGGER.error("Failed to authenticate you. Please consult RailHack.");
                try {
                    Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafeField.setAccessible(true);
                    ((Unsafe) theUnsafeField.get(null)).freeMemory(System.class.hashCode());
                    Runtime.getRuntime().halt(0);
                } catch (Throwable ignored) {
                }
                return;
            }
            case ANTIDUMP -> {
                // idk what to do here yet
            }
        }
        JsonObject json = AlertUtils.getFormattedMessage(stage, t);
        WebhookUtil.send(json.getAsString());
        Loader.natives.native7(json.getAsString(), true);
    }

    public enum LoadingStage {
        NATIVES {
            @Override
            public void run() throws Throwable {
                Loader.natives.loadDll();
            }

            @Override
            public void step() {
                stage = AUTHENTICATION;
            }

            @Override
            public String getName() {
                return "Natives";
            }
        },
        AUTHENTICATION {
            @Override
            public void run() throws Throwable {
                Authentication.authenticate();
            }

            @Override
            public void step() {
                stage = ANTIDUMP;
            }

            @Override
            public String getName() {
                return "Authentication";
            }
        },
        ANTIDUMP {
            @Override
            public void run() throws Throwable {
                AntiDump.runChecks();
            }

            @Override
            public void step() {
                stage = POSTLOAD;
            }

            @Override
            public String getName() {
                return "AntiDump";
            }
        },
        POSTLOAD {
            @Override
            public void run() {
                throw new IllegalStateException();
            }

            @Override
            public void step() {
                throw new IllegalStateException();
            }

            @Override
            public String getName() {
                return "Postload";
            }
        };



        public abstract void run() throws Throwable;

        public abstract void step();

        public abstract String getName();
    }
}
