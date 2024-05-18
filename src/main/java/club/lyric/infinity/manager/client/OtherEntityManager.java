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
public final class OtherEntityManager implements IMinecraft {
    private final Map<Integer, Integer> totemPopMap = new HashMap<>();
    public void onTotemPop(Entity playerEntity)
    {
        Integer amount = totemPopMap.get(playerEntity.getId());
        totemPopMap.put(playerEntity.getId(), amount == null ? 1 : amount + 1);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).totemPops.value())
        {
            ChatUtils.sendOverwriteMessageColored(getAppropriateFormatting((PlayerEntity) playerEntity) + getName((PlayerEntity) playerEntity) + Formatting.RESET + " popped " + Formatting.WHITE + amount + Formatting.RESET + (amount < 2 ? " totem." : " totems."), playerEntity.getId());
        }
    }

    public void onDeath(PlayerEntity playerEntity)
    {
        Integer amount = totemPopMap.get(playerEntity.getId());
        totemPopMap.replace(playerEntity.getId(), 0);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).totemPops.value())
        {
            ChatUtils.sendOverwriteMessageColored(getAppropriateFormatting(playerEntity) + getName(playerEntity) + Formatting.RESET + " died after popping " + Formatting.WHITE + amount + Formatting.RESET + (amount < 2 ? " totem." : " totems."), playerEntity.getId());
        }
    }
    public void unload()
    {
        totemPopMap.clear();
    }

    public Formatting getAppropriateFormatting(PlayerEntity player)
    {
        if (player == mc.player)
        {
            return Formatting.GREEN;
        }
        if (Managers.FRIENDS.isFriend(player))
        {
            return Formatting.AQUA;
        }
        return Formatting.WHITE;
    }

    private String getName(PlayerEntity player)
    {
        if (player == mc.player) return "You";
        return player.getDisplayName().getString();
    }
}
