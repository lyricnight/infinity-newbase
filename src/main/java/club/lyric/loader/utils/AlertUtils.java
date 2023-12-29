package club.lyric.loader.utils;

import club.lyric.loader.stage.ClientLoader;
import club.lyric.loader.stage.authentication.Authentication;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.awt.*;

public class AlertUtils {

    public static JsonObject getFormattedMessage(ClientLoader.LoadingStage loadingStage, Throwable cause) {
        String computerName = System.getProperty("user.name");
        StringBuilder modlistBuilder = new StringBuilder();
        for (String s : Authentication.getModList()) {
            modlistBuilder.append(s).append(", ");
        }
        String modlist = modlistBuilder.toString();

        JsonObject content = new JsonObject();
        content.add("content", new JsonPrimitive("@everyone"));
        content.add("username", new JsonPrimitive("Infinity"));
        content.add("avatar_url", new JsonPrimitive("https://media.discordapp.net/attachments/1176728790169296936/1189386168723320832/6b468e8f41d3a1ba4567e4e18a6169096e411843.png?ex=659df930&is=658b8430&hm=bb4e1050bddd4bfc095dfc55a9a6d41596d9a0a0c2bedbb9e36a769de34378f2&=&format=webp&quality=lossless&width=730&height=702"));
        content.add("tts", new JsonPrimitive(false));

        JsonArray embeds = new JsonArray();

        JsonObject embed = new JsonObject();
        embed.add("title", new JsonPrimitive("Loader Alert"));
        Color red = new Color(236, 98, 69);
        int color = red.getRed();
        color = (color << 8) + red.getGreen();
        color = (color << 8) + red.getBlue();
        embed.add("color", new JsonPrimitive(color));

        JsonObject footer = new JsonObject();
        footer.add("text", new JsonPrimitive("\u00A9 Infinity"));
        footer.add("icon_url", new JsonPrimitive("https://media.discordapp.net/attachments/1176728790169296936/1189386168723320832/6b468e8f41d3a1ba4567e4e18a6169096e411843.png?ex=659df930&is=658b8430&hm=bb4e1050bddd4bfc095dfc55a9a6d41596d9a0a0c2bedbb9e36a769de34378f2&=&format=webp&quality=lossless&width=730&height=702"));

        embed.add("footer", footer);

        JsonArray fields = new JsonArray();

        JsonObject reasonField = new JsonObject();
        reasonField.add("name", new JsonPrimitive("Reason"));
        reasonField.add("value", new JsonPrimitive(cause.toString()));
        reasonField.add("inline", new JsonPrimitive(true));

        JsonObject stageField = new JsonObject();
        stageField.add("name", new JsonPrimitive("Stage"));
        stageField.add("value", new JsonPrimitive(loadingStage.getName()));
        stageField.add("inline", new JsonPrimitive(true));

        JsonObject computerNameField = new JsonObject();
        computerNameField.add("name", new JsonPrimitive("Computer Name"));
        computerNameField.add("value", new JsonPrimitive(computerName));
        computerNameField.add("inline", new JsonPrimitive(true));

        JsonObject modsField = new JsonObject();
        modsField.add("name", new JsonPrimitive("Mods"));
        modsField.add("value", new JsonPrimitive(modlist));
        modsField.add("inline", new JsonPrimitive(true));

        fields.add(reasonField);
        fields.add(stageField);
        fields.add(computerNameField);
        fields.add(modsField);
        embed.add("fields", fields);

        embeds.add(embed);
        content.add("embeds", embeds);

        return content;
    }
}
