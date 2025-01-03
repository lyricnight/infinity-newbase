package club.lyric.infinity.api.util.client.web;

import club.lyric.infinity.Infinity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/**
 * @author vasler
 */
public class UUIDConverter {

    public static String getPlayerName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";

        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();

            return jsonObject.get("name").getAsString();

        } catch (IOException e) {
            Infinity.LOGGER.error(e);
        }

        return null;
    }
}
