package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

/**
 * @author lyric
 * 'services are overrated'
 */
public class InventoryManager implements IMinecraft {
    /**
     * represents the slot in use
     */
    private int s;

    /**
     * sends the swap packet, doesn't have any checks.
     * @param slot - int value of slot to swap to
     */
    public void setSlotPacket(int slot)
    {
        if (s != slot && PlayerInventory.isValidHotbarIndex(slot))
        {
            sendPacket(slot);
        }
    }

    /**
     * swaps both client and server-side - swap mode 'normal'
     * @param slot - int value of slot to swap to
     */
    public void setSlotFull(int slot)
    {
        if (getClientSlot() != slot && PlayerInventory.isValidHotbarIndex(slot))
        {
            mc.player.getInventory().selectedSlot = slot;
            sendPacket(slot);
        }
    }

    /**
     * call when a desync is detected: causes us to send a packet swap to client slot.
     * for safety, won't activate unless there is actually a desync present
     */

    public void forceSync()
    {
        if (isDesynced()) sendPacket(mc.player.getInventory().selectedSlot);
    }

    /**
     * p
     * @return - slot (serverside)
     */
    public int getSlot() {
        return s;
    }

    /**
     * m
     * @return - slot (clientside)
     */
    public int getClientSlot()
    {
        return mc.player.getInventory().selectedSlot;
    }

    /**
     * private convenience functions
     */
    private void sendPacket(int slot)
    {
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    /**
     * m
     * @return - are we desynced
     */
    public boolean isDesynced()
    {
        return getClientSlot() != s;
    }

    /**
     * event methods that update values of s
     */

    @EventHandler
    public void onPacketSend(PacketEvent.Send event)
    {
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet)
        {
            int slot = packet.getSelectedSlot();
            if (!PlayerInventory.isValidHotbarIndex(slot) || s == slot)
            {
                event.setCancelled(true);
                return;
            }
            s = slot;
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event)
    {
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket packet)
        {
            s = packet.getSlot();
        }
    }
}
