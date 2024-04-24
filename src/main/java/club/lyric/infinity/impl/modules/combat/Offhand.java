package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

@SuppressWarnings("ConstantConditions")
public class Offhand extends ModuleBase
{

    public Offhand()
    {
        super("Offhand", "Allows you to put different items in your offhand slot.", Category.Combat);
    }

    protected Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();


    @Override
    public void onUpdate()
    {
        ScreenHandler screenHandler = mc.player.currentScreenHandler;
        //Item i = getItemType();

        //if (mc.player.getOffHandStack().getItem() != i)
        //{

            if (mc.currentScreen instanceof InventoryScreen) mc.player.closeScreen();

            //int slot = GetItemSlot(i);

            //if (slot != -1) return;

            //send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), slot, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));
            send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), 45, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));
            //send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), slot, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));


        //}
    }
}
