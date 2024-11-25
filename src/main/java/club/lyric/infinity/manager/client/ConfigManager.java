package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.*;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ???
 */

public final class ConfigManager implements IMinecraft {
    private final Gson GSON = new Gson();
    private final Path pathConfigFolder;
    private final Path pathProfilesFolder;
    private final Path pathConfig;
    private JsonObject jsonConfig;
    public ConfigProfile currentProfile;
    public final Map<Path, ConfigProfile> profiles = new HashMap<>();
    private final Map<String, ConfigProfile> profilesByName = new HashMap<>();
    private String[] namesArray;

    public ConfigManager() {
        String folderDirectory = mc.runDirectory.getAbsolutePath();
        pathConfigFolder = Paths.get(folderDirectory).resolve("infinity");
        pathProfilesFolder = pathConfigFolder.resolve("profiles");

        pathConfig = pathConfigFolder.resolve("infinity.json");

        File[] files = pathProfilesFolder.toFile().listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Path pathProfile = file.toPath();

                    if (pathProfile.toString().contains(".json")) {
                        addProfile(new ConfigProfile(null, pathProfile));
                    }
                }
            }
        }
    }

    public void addProfile(ConfigProfile configProfile) {
        if (!profiles.containsKey(configProfile.getPathProfile())) {
            profiles.put(configProfile.getPathProfile(), configProfile);
            profilesByName.put(configProfile.getName(), configProfile);
            namesArray = profilesByName.keySet().toArray(String[]::new);
        } else {
            addProfile(configProfile.setPathProfile(pathProfilesFolder.resolve(configProfile.getPathProfile().getFileName().toString().replace(".json", "1.json"))));
        }
    }

    public void removeProfile(ConfigProfile configProfile) {
        profiles.remove(configProfile.getPathProfile());
    }

    public void loadConfig() {
        try {
            if (Files.isRegularFile(pathConfig)) {
                jsonConfig = GSON.fromJson(Files.readString(pathConfig), JsonObject.class);

                JsonElement profile = jsonConfig.get("profile");

                if (profile == null) {
                    currentProfile = new ConfigProfile("Default", pathProfilesFolder.resolve("default.json"));
                } else {
                    currentProfile = profiles.get(Paths.get(profile.getAsString()));
                }
            } else {
                currentProfile = new ConfigProfile("Default", pathProfilesFolder.resolve("default.json"));
            }

            if (namesArray == null) {
                addProfile(currentProfile);
            }

            currentProfile.loadProfile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        try {
            Files.createDirectories(pathConfigFolder);

            jsonConfig = new JsonObject();

            jsonConfig.addProperty("profile", currentProfile.getPathProfile().toString());

            Files.writeString(pathConfig, GSON.toJson(jsonConfig));

            currentProfile.saveProfile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class ConfigProfile {
        private String name;
        private Path pathProfile;
        private JsonObject jsonProfile;

        public ConfigProfile(String name, Path pathProfile) {
            this.name = name;
            this.pathProfile = pathProfile;

            try {
                if (name == null || name.isBlank()) {
                    if (Files.isRegularFile(pathProfile)) {
                        String stringProfile = Files.readString(pathProfile);

                        if (stringProfile.isBlank()) return;

                        this.jsonProfile = GSON.fromJson(stringProfile, JsonObject.class);

                        if (jsonProfile == null || !jsonProfile.isJsonObject()) return;

                        JsonElement profileNameJson = this.jsonProfile.get("profileName");

                        if (profileNameJson == null) return;

                        this.name = profileNameJson.getAsString();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String getName() {
            return name;
        }

        public Path getPathProfile() {
            return pathProfile;
        }

        public ConfigProfile setPathProfile(Path pathProfile) {
            this.pathProfile = pathProfile;
            return this;
        }

        public void loadProfile() {
            try {
                if (!Files.isRegularFile(pathProfile))
                    return;

                for (ModuleBase moduleBase : Managers.MODULES.getModules()) {
                    JsonElement moduleJson = jsonProfile.get(moduleBase.getName());
                    if (moduleJson == null || !moduleJson.isJsonObject())
                        continue;
                    JsonObject moduleConfig = moduleJson.getAsJsonObject();

                    JsonElement enabledJson = moduleConfig.get("enabled");
                    if (enabledJson == null || !enabledJson.isJsonPrimitive())
                        continue;

                    if (enabledJson.getAsBoolean())
                        moduleBase.setEnabled(true);

                    for (Setting setting : moduleBase.getSettings()) {
                        JsonElement settingJson = moduleConfig.get(setting.getName());
                        if (settingJson == null)
                            continue;

                        if (setting instanceof BooleanSetting booleanSetting) {
                            booleanSetting.setValue(settingJson.getAsBoolean());
                        } else if (setting instanceof BindSetting bindSetting) {
                            bindSetting.setCode(settingJson.getAsInt());
                        } else if (setting instanceof ModeSetting modeSetting) {
                            modeSetting.setMode(settingJson.getAsString());
                        } else if (setting instanceof NumberSetting numberSetting) {
                            numberSetting.setValue(settingJson.getAsDouble());
                        } else if (setting instanceof ColorSetting colorSetting) {
                            if (!settingJson.isJsonObject())
                                continue;

                            JsonObject colorJson = settingJson.getAsJsonObject();
                            colorSetting.setColor(new JColor(colorJson.get("color").getAsInt()));
                        }
                    }
                }

                JsonElement friendsJson = this.jsonProfile.get("friends");
                if (friendsJson != null && friendsJson.isJsonObject())
                    Managers.FRIENDS.setFriends(friendsJson.getAsJsonObject());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void saveProfile() {
            try {
                Files.createDirectories(pathProfilesFolder);

                jsonProfile = new JsonObject();

                jsonProfile.addProperty("profileName", this.name);

                for (ModuleBase moduleBase : Managers.MODULES.getModules()) {
                    JsonObject moduleConfig = new JsonObject();

                    moduleConfig.addProperty("enabled", moduleBase.isOn());
                    for (Setting setting : moduleBase.getSettings()) {
                        if (setting instanceof BooleanSetting booleanSetting) {
                            moduleConfig.addProperty(setting.getName(), booleanSetting.value());
                        } else if (setting instanceof BindSetting bindSetting) {
                            moduleConfig.addProperty(setting.getName(), bindSetting.getCode());
                        } else if (setting instanceof ModeSetting modeSetting) {
                            moduleConfig.addProperty(setting.getName(), modeSetting.getMode());
                        } else if (setting instanceof NumberSetting numberSetting) {
                            moduleConfig.addProperty(setting.getName(), numberSetting.getValue());
                        } else if (setting instanceof ColorSetting colorSetting) {
                            JsonObject colorJson = new JsonObject();
                            colorJson.addProperty("color", colorSetting.getValue().getRGB());
                            moduleConfig.add(setting.getName(), colorJson);
                        }
                    }

                    jsonProfile.add(moduleBase.getName(), moduleConfig);
                }

                jsonProfile.add("friends", Managers.FRIENDS.getFriends());

                Files.writeString(pathProfile, GSON.toJson(jsonProfile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

