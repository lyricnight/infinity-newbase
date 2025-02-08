package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lyric
 * handles other players.
 */
public final class OtherEntityManager implements IMinecraft {
    /**
     * hashmap of player id to totem pops.
     */
    public final Map<Integer, Integer> totemPopMap = new HashMap<>();

    /**
     * called when a player pops a totem
     * @see club.lyric.infinity.asm.MixinClientPlayNetworkHandler
     * @param playerEntity - player to check.
     */
    public void onTotemPop(PlayerEntity playerEntity)
    {
        Integer amount = totemPopMap.get(playerEntity.getId());
        totemPopMap.put(playerEntity.getId(), amount == null ? 1 : amount + 1);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).totemPops.value())
        {
            if (amount == null)
            {
                Managers.MESSAGES.sendOverwriteMessage(getAppropriateFormatting(playerEntity) + getName(playerEntity) + Formatting.RESET + " popped " + Formatting.WHITE + "1" + Formatting.RESET + " totem.", playerEntity.getId(), true);
            }
            else
            {
                Managers.MESSAGES.sendOverwriteMessage(getAppropriateFormatting(playerEntity) + getName(playerEntity) + Formatting.RESET + " popped " + Formatting.WHITE + amount + Formatting.RESET + (amount < 2 ? " totem." : " totems."), playerEntity.getId(), true);
            }
        }
    }

    /**
     * called when a player dies.
     * @param playerEntity - player to mark as dead
     * @see club.lyric.infinity.asm.MixinDataTracker
     */
    public void onDeath(PlayerEntity playerEntity)
    {
        Integer amount = totemPopMap.get(playerEntity.getId());
        totemPopMap.replace(playerEntity.getId(), 0);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).totemPops.value())
        {
            Managers.MESSAGES.sendOverwriteMessage(getAppropriateFormatting(playerEntity) + getName(playerEntity) + Formatting.RESET + " died after popping " + Formatting.WHITE + amount + Formatting.RESET + (amount < 2 ? " totem." : " totems."), playerEntity.getId(), true);
        }
    }

    /**
     * called when a player is added to our render
     * @param player - player to add
     * @see club.lyric.infinity.asm.MixinClientWorld
     */
    public void onAddEntity(PlayerEntity player)
    {
        if (Null.is()) return;
        if (Managers.MODULES.getModuleFromClass(Notifications.class).render.value()) {
            if (player.getName().getString().equalsIgnoreCase(mc.player.getName().getString())) return;
            Managers.MESSAGES.sendOverwriteMessage(getAppropriateFormatting(player) + player.getDisplayName().getString() + Formatting.RESET + " has entered your render.", player.getId(), true);
        }
    }

    /**
     * called when a player is removed from our render.
     * @param player - player to remove
     * @see club.lyric.infinity.asm.MixinClientWorld
     */
    public void onRemoveEntity(PlayerEntity player)
    {
        if (Null.is()) return;
        if (Managers.MODULES.getModuleFromClass(Notifications.class).render.value()) {
            if (player.getName().getString().equalsIgnoreCase(mc.player.getName().getString())) return;
            Managers.MESSAGES.sendOverwriteMessage(getAppropriateFormatting(player) + player.getDisplayName().getString() + Formatting.RESET + " has left your render.", player.getId(), true);
        }
    }

    /**
     * called when the manager is unloaded.
     */
    public void unload()
    {
        totemPopMap.clear();
    }

    /**
     * can be used for nametags.
     * @param playerEntity - player to check pops for.
     * @return - number of totems popped by that player.
     */
    public int getTotemPops(PlayerEntity playerEntity)
    {
        return totemPopMap.getOrDefault(playerEntity.getId(), 0);
    }

    /**
     * gets the closest player to you that we can target.
     * @param range - range to check
     * @return target.
     */
    public PlayerEntity getTarget(double range)
    {
        if (Null.is()) return null;
        return mc.world.getPlayers().stream().filter(Objects::nonNull).filter(LivingEntity::isAlive).filter(entity -> entity != mc.player).filter(entity -> entity.getId() != mc.player.getId()).filter(entity -> !Managers.FRIENDS.isFriend(entity)).filter(entity -> mc.player.squaredDistanceTo(entity) <= MathUtils.square(range)).min(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity))).orElse(null);
    }

    //private methods!

    /**
     * convenience function
     * @param player - player
     * @return correct Formatting
     */
    private Formatting getAppropriateFormatting(PlayerEntity player)
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
