package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;

public class InventoryUtils implements IMinecraft {

    public static FindItems findOptimalTool(BlockState state) {
        float bestScore = 1;
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isSuitableFor(state)) continue;

            float score = stack.getMiningSpeedMultiplier(state);
            if (score > bestScore) {
                bestScore = score;
                slot = i;
            }
        }

        return new FindItems(slot, 1);
    }

    public static int getBedsCount() {
        if (mc.player == null) return 0;

        int counter = 0;

        for (int i = 0; i <= 44; ++i) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (!(itemStack.getItem() instanceof BedItem)) continue;
            counter += itemStack.getCount();
        }

        return counter;
    }

    public static int getDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    public static float getPercent(ItemStack stack) {
        return (getDamage(stack) / (float) stack.getMaxDamage()) * 100.0f;
    }
}
