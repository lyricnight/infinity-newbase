package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

/**
 * @author vasler
 */
public final class Replenish extends ModuleBase {
    public NumberSetting threshold = new NumberSetting("Threshold", this, 24.0f, 1.0f, 64.0f, 1f, " items");
    public NumberSetting pearlThreshold = new NumberSetting("PearlThreshold", this, 4.0f, 1.0f, 16.0f, 1f, " items");

    public Replenish()
    {
        super("Replenish", "", Category.MISC);
    }

    @Override
    public void onUpdate()
    {
        if (mc.currentScreen != null) return;

        for (int slot = 0; slot < 9; ++slot)
        {
            if (replenishItemSlot(slot))
                return;
        }
    }

    private boolean replenishItemSlot(int slot)
    {
        PlayerInventory inventory = mc.player.getInventory();
        ItemStack stack = inventory.getStack(slot);

        if (isRefillable(stack))
        {
            for (int i = 9; i < 36; ++i)
            {

                ItemStack itemStack = inventory.getStack(i);

                if (!itemStack.isEmpty() && InventoryUtils.areItemsEqual(stack, itemStack))
                {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                    return true;
                }

            }

        }
        return false;
    }

    private boolean isRefillable(ItemStack stack)
    {
        if (stack.getItem() == Items.ENDER_PEARL)
        {
            return !stack.isEmpty() &&
                    stack.getCount() <= pearlThreshold.getValue() &&
                    stack.getItem() != Items.AIR &&
                    stack.isStackable() &&
                    stack.getCount() < stack.getMaxCount();
        }

        return !stack.isEmpty() &&
                stack.getCount() <= threshold.getValue() &&
                stack.getItem() != Items.AIR &&
                stack.isStackable() &&
                stack.getCount() < stack.getMaxCount();
    }
}
