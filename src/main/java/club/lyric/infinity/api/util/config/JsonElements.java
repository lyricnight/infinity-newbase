package club.lyric.infinity.api.util.config;

import com.google.gson.JsonElement;

/**
 * @author lyric
 * for saving friends and command prefix
 */
public interface JsonElements {
    JsonElement toJson();
    void fromJson(JsonElement element);
    default String getFileName() {
        return "";
    }
}
