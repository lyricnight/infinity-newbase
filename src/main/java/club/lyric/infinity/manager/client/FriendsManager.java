package club.lyric.infinity.manager.client;

import club.lyric.infinity.manager.Managers;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author lyric
 */
@Setter
@Getter
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
            Managers.MESSAGES.sendOverwriteMessage(name + " is already on your friends list!", 9349, false);
            return;
        }
        this.friends.add(getUniqueIdentifier(name), new JsonPrimitive(name));
        Managers.MESSAGES.sendOverwriteMessage("Added " + name + " to your friends list.", 9349, false);
    }

    public void removeFriend(String name)
    {
        if (isFriend(name))
        {
            this.friends.remove(getUniqueIdentifier(name));
            Managers.MESSAGES.sendOverwriteMessage("Removed " + name + " from your friends list.", 9349, false);
        }
        else
        {
            Managers.MESSAGES.sendOverwriteMessage(name + " is not on your friends list!", 9349, false);
        }
    }

    private String getUniqueIdentifier(String name)
    {
        return String.valueOf(name.hashCode());
    }
}