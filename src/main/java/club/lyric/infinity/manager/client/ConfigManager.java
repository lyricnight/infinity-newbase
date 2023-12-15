package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.Bind;
import club.lyric.infinity.api.setting.settings.EnumConverter;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.manager.Managers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author cattyn???
 * this needs to be replaced
 */

public class ConfigManager {
    private static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("Infinity");
    private static final Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private List<JsonElements> jsonElements;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setValueFromJson(Setting setting, JsonElement element) {
        String str;
        switch (setting.getType()) {
            case "Boolean" -> {
                setting.setValue(element.getAsBoolean());
            }
            case "Double" -> {
                setting.setValue(element.getAsDouble());
            }
            case "Float" -> {
                setting.setValue(element.getAsFloat());
            }
            case "Integer" -> {
                setting.setValue(element.getAsInt());
            }
            case "String" -> {
                str = element.getAsString();
                setting.setValue(str.replace("_", " "));
            }
            case "Bind" -> {
                setting.setValue(new Bind(element.getAsInt()));
            }
            case "Enum" -> {
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }


    /**
     * avoids crash
     */

    public void init()
    {
        jsonElements = List.of(Infinity.COMMANDS, Infinity.MODULES, Infinity.FRIENDS);
    }


    /**
     * loads config
     */
    public void load() {
        if (!PATH.toFile().exists()) PATH.toFile().mkdirs();
        for (JsonElements jsonElements : jsonElements) {
            try {
                String read = Files.readString(PATH.resolve(jsonElements.getFileName()));
                jsonElements.fromJson(JsonParser.parseString(read));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * saves config
     */
    public void save() {
        if (!PATH.toFile().exists()) PATH.toFile().mkdirs();
        for (JsonElements jsonElements : jsonElements) {
            try {
                JsonElement json = jsonElements.toJson();
                Files.writeString(PATH.resolve(jsonElements.getFileName()), gson.toJson(json));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
