package club.lyric.infinity.api.module;

import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.manager.Managers;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

/**
 * @author lyric
 */
public class BlockModuleBase extends ModuleBase {
    /**
     * represents priority for rotations and block placements
     */
    private final int prio;

    public BlockModuleBase(String name, String description, Category category, int prio) {
        super(name, description, category);
        this.prio = prio;
    }

    /**
     * specific methods
     */

    protected int getBlockSlot(Block block) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem item && item.getBlock() == block) {
                return i;
            }
        }
        return -1;
    }

    protected void setRotationPoint(float yaw, float pitch) {
        Managers.ROTATIONS.setRotationPoint(new RotationPoint(yaw, pitch, getPriority(), false));
    }

    protected boolean blocked() {
        return Managers.ROTATIONS.isRotationLate(getPriority());
    }

    public int getPriority() {
        return prio;
    }
}