package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

/**
 * @author lyric
 * 'services are overrated'
 */
public final class InventoryManager implements IMinecraft {
    private ItemStack stack = null;

    private ItemStack stackAlt = null;

    /**
     * convenience variable, for slot swapping.
     */
    private int last = -1;

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
     * TODO
     * uses the alternative swap slot method to swap.
     * @param slot - int value of slot we swap to.
     */
    public void setSlotSwap(int slot)
    {
        last = mc.player.getInventory().selectedSlot;
        stackAlt = mc.player.getMainHandStack();
        slot = convert(slot);
        if (mc.player.getInventory().selectedSlot != slot && slot > 35 && slot < 45) {}
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

    /**
     * converts itemstack to slot.
     * @param slot - slot in
     * @return converted slot.
     */
    private int convert(int slot)
    {
        if (slot == -2) {
            return 45;
        }

        if (slot > -1 && slot < 9) {
            return 36 + slot;
        }

        return slot;
    }

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
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket updateSelectedSlotS2CPacket)
        {
            s = updateSelectedSlotS2CPacket.getSlot();
        }
    }
}
