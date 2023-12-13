package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.util.chat.ChatUtils;
import club.lyric.infinity.api.util.chat.ID;
import club.lyric.infinity.api.util.config.JsonElements;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class FriendsManager implements JsonElements {
    private final List<String> friends = new ArrayList<>();

    public boolean isFriend(String name) {
        return this.friends.stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }

    public boolean isFriend(PlayerEntity player) {
        return this.isFriend(player.getGameProfile().getName());
    }

    public void addFriend(String name) {
        if (isFriend(name))
        {
            ChatUtils.sendOverwriteMessage(name + " is already on your friends list!", ID.FRIEND);
            return;
        }
        this.friends.add(name);
        ChatUtils.sendOverwriteMessage("Added " + name + " to your friends list.", ID.FRIEND);
    }

    public void removeFriend(String name) {
        if (friends.contains(name))
        {
            ChatUtils.sendOverwriteMessage("Removed " + name + " from your friends list.", ID.FRIEND);
            friends.removeIf(s -> s.equalsIgnoreCase(name));
        }
        else
        {
            ChatUtils.sendOverwriteMessage( name + " is not on your friends list!", ID.FRIEND);
        }
    }

    public List<String> getFriends() {
        return this.friends;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (String friend : friends) {
            array.add(friend);
        }
        object.add("friends", array);
        return object;
    }

    @Override
    public void fromJson(JsonElement element) {
        for (JsonElement e : element.getAsJsonObject().get("friends").getAsJsonArray()) {
            friends.add(e.getAsString());
        }
    }

    @Override
    public String getFileName() {
        return "friends.json";
    }
}