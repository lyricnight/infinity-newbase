package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.item.ItemStack;

public class InventoryUtils implements IMinecraft {
    public static boolean areItemsEqual(ItemStack left, ItemStack right)
    {
        return left.getItem() == right.getItem() &&
                ItemStack.areItemsEqual(left, right) &&
                (!left.isDamageable() || left.getDamage() == right.getDamage());
    }

    public static int getDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    public static float getPercent(ItemStack stack) {
        return (getDamage(stack) / (float) stack.getMaxDamage()) * 100.0f;
    }
}
