package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyric
 * handles other player totem pops and player deaths
 */
//TODO: finish
public class OtherEntityManager implements IMinecraft {
    private Map<Integer, Integer> totemPopMap = new HashMap<>();
    public void onTotemPop(Entity playerEntity)
    {
        Integer amount = totemPopMap.get(playerEntity.getId());
        totemPopMap.put(playerEntity.getId(), amount == null ? 1 : amount + 1);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).totemPops.value())
        {
            ChatUtils.sendOverwriteMessage(Formatting.BOLD + playerEntity.getDisplayName().getString() + " popped " + amount + (amount < 2 ? " totem." : " totems."), playerEntity.getId());
        }
    }

    public void onDeath(PlayerEntity playerEntity)
    {

    }



    public void unload()
    {
        totemPopMap.clear();
    }
}
