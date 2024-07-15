package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;

public class AutoTool extends ModuleBase {

    int previousSlot;
    boolean pressed;

    public AutoTool()
    {
        super("AutoTool", "aa", Category.Misc);
    }

    @Override
    public void onUpdate()
    {
        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

        if (!InventoryUtils.findOptimalTool(mc.world.getBlockState(result.getBlockPos())).found()) return;

        if (mc.options.attackKey.isPressed())
        {
            pressed = true;
            previousSlot = mc.player.getInventory().selectedSlot;

            Managers.INVENTORY.setSlotFull(InventoryUtils.findOptimalTool(mc.world.getBlockState(result.getBlockPos())).slot());
        }

        if (!mc.options.attackKey.isPressed() && pressed)
        {
            Managers.INVENTORY.setSlotFull(previousSlot);
            pressed = false;
        }
    }
}
