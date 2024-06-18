package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.util.client.chat.ChatUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author lyric
 */
public final class FriendsManager  {
    private JsonObject friends = new JsonObject();

    public boolean isFriend(PlayerEntity player) {
        return friends.get(String.valueOf(player.hashCode())) != null;
    }

    public boolean isFriend(String name)
    {
        return friends.get(getUniqueIdentifier(name)) != null;
    }

    public void addFriend(String name)
    {
        if (isFriend(name))
        {
            ChatUtils.sendOverwriteMessage(name + " is already on your friends list!", 9349);
            return;
        }
        this.friends.add(getUniqueIdentifier(name), new JsonPrimitive(name));
        ChatUtils.sendOverwriteMessage("Added " + name + " to your friends list.", 9349);
    }

    public void removeFriend(String name)
    {
        if (isFriend(name))
        {
            this.friends.remove(getUniqueIdentifier(name));
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

    private String getUniqueIdentifier(String name)
    {
        return String.valueOf(name.hashCode());
    }
}