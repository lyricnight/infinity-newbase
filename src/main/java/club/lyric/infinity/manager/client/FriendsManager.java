package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.util.client.chat.ChatUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.PlayerEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * @author lyric
 */
@SuppressWarnings("ConstantConditions")
public final class FriendsManager  {
    private JsonObject friends = new JsonObject();

    public boolean isFriend(PlayerEntity player) {
        return friends.get(player.getUuidAsString()) != null;
    }

    public boolean isFriend(String name)
    {
        return friends.get(getUUID(name)) != null;
    }

    public void addFriend(String name)
    {
        if (isFriend(name))
        {
            ChatUtils.sendOverwriteMessage(name + " is already on your friends list!", 9349);
            return;
        }
        this.friends.add(getUUID(name), new JsonPrimitive(name));
        ChatUtils.sendOverwriteMessage("Added " + name + " to your friends list.", 9349);
    }

    public void removeFriend(String name)
    {
        if (isFriend(name))
        {
            this.friends.remove(getUUID(name));
            ChatUtils.sendOverwriteMessage("Removed " + name + " from your friends list.", 9349);
        }
        else
        {
            ChatUtils.sendOverwriteMessage(name + " is not on your friends list!", 9349);
        }
    }

    public JsonObject getFriends() {
        return friends;
    }

    public void setFriends(JsonObject friends)
    {
        this.friends = friends;
    }

    @SuppressWarnings("deprecated")
    public static String getUUID(String name) {
        String uuid;
        try {
            //noinspection BlockingMethodInNonBlockingContext
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = ((JsonObject)new JsonParser().parse(bufferedReader)).get("id").toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            //noinspection BlockingMethodInNonBlockingContext
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException("UUID Error -> couldn't get the uuid of:" + name);
        }
        return uuid;
    }


}