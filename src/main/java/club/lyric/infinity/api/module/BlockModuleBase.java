package club.lyric.infinity.api.module;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.minecraft.rotation.RotationHandler;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import club.lyric.infinity.manager.Managers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author lyric
 */
public class BlockModuleBase extends ModuleBase {
    /**
     * represents priority for rotations and block placements
     */
    private final int prio;

    /**
     * list of placeable blocks, sorted in order of preference.
     */
    private static final List<Block> PLACEABLE_BLOCKS = new LinkedList<>() {{
        add(Blocks.OBSIDIAN);
        add(Blocks.CRYING_OBSIDIAN);
        add(Blocks.ENDER_CHEST);
    }};

    /**
     * constructor
     * @param name - name
     * @param description - desc
     * @param category - category
     * @param prio - prio
     */
    public BlockModuleBase(String name, String description, Category category, int prio)
    {
        super(name, description, category);
        this.prio = prio;
    }

    /**
     * specific methods
     */

    protected int getPlaceableBlockSlot()
    {
        for (Block block : PLACEABLE_BLOCKS)
        {
            int slot = getBlockSlot(block);
            if (slot != -1)
            {
                return slot;
            }
        }
        return -1;
    }

    protected int getBlockSlot(Block block)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() == block)
            {
                return i;
            }
        }
        return -1;
    }

    protected void setRotationPoint(float yaw, float pitch)
    {
        Managers.ROTATIONS.setRotationPoint(new RotationPoint(yaw, pitch, getPriority(), false));
    }

    protected boolean blocked() {
        return Managers.ROTATIONS.isRotationLate(getPriority());
    }

    public int getPriority() {
        return prio;
    }

    /**
     * vasler this is the method you call to place a block
     * to construct a RotationHandler, you write something like this:
     * <code>
     *  placeBlock(pos, slot (state, angles) -> if (AntiCheat.getRotations()))
     *  {
     *      if state -> rotate normally
     *      else rotate sync
     *  }
     *  else do nothing because no rotation.
     * </code>
     */
    protected boolean placeBlock(BlockPos pos, int slot, RotationHandler rotationHandler)
    {
        Direction direction = getDirection(pos);
        if (direction == null && !AntiCheat.getStrictDirection())
        {
            Infinity.LOGGER.info("Logged no direction, defaulting.");
            direction = Direction.UP;
        }
        if (direction == null)
        {
            return false;
        }
        BlockPos neighbor = pos.offset(direction.getOpposite());
        return placeBlock(neighbor, direction, slot, rotationHandler);
    }

    private boolean placeBlock(BlockPos pos, Direction direction, int slot, RotationHandler rotationHandler)
    {
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d(direction.getUnitVector()).multiply(0.5));
        return placeBlock(new BlockHitResult(hitVec, direction, pos, false), slot, rotationHandler);
    }

    private boolean placeBlock(BlockHitResult hitResult, int slot, RotationHandler rotationHandler)
    {
        boolean isSpoofing = slot != Managers.INVENTORY.getSlot();
        if (isSpoofing)
        {
            Managers.INVENTORY.setSlotPacket(slot);
        }

        boolean isRotating = rotationHandler != null;
        if (isRotating)
        {
            float[] angles = RotationUtils.getRotationsTo(mc.player.getEyePos(), hitResult.getPos());
            rotationHandler.handle(true, angles);
        }

        boolean result = place(hitResult);
        if (isRotating)
        {
            float[] angles = RotationUtils.getRotationsTo(mc.player.getEyePos(), hitResult.getPos());
            rotationHandler.handle(false, angles);
        }

        if (isSpoofing)
        {
            Managers.INVENTORY.forceSync();
        }

        return result;
    }

    private boolean place(BlockHitResult result)
    {
        ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result);;
        if (actionResult.isAccepted() && actionResult.shouldSwingHand())
        {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
        return actionResult.isAccepted();
    }
    /**
     * gets direction for block placement.
     */
    private Direction getDirection(BlockPos blockPos)
    {
        Set<Direction> validDirectionsSet = getDirectionsNCP(mc.player.getEyePos().x,mc.player.getEyePos().y, mc.player.getEyePos().z, blockPos.toCenterPos().x, blockPos.toCenterPos().y, blockPos.toCenterPos().z);
        Direction interactDirection = null;
        for (Direction direction : Direction.values())
        {
            BlockState state = mc.world.getBlockState(blockPos.offset(direction));
            if (state.isAir() || !state.getFluidState().isEmpty())
            {
                continue;
            }
            if (AntiCheat.getStrictDirection() && !validDirectionsSet.contains(direction.getOpposite()))
            {
                continue;
            }
            interactDirection = direction;
            break;
        }
        if (interactDirection == null)
        {
            return null;
        }
        return interactDirection.getOpposite();
    }

    /**
     * ncp directions
     */
    private Set<Direction> getDirectionsNCP(double x, double y, double z, double dx, double dy, double dz)
    {
        double xdiff = x - dx;
        double ydiff = y - dy;
        double zdiff = z - dz;
        Set<Direction> dirs = new HashSet<>(6);
        if (ydiff > 0.5) {
            dirs.add(Direction.UP);
        } else if (ydiff < -0.5) {
            dirs.add(Direction.DOWN);
        } else {
            dirs.add(Direction.UP);
            dirs.add(Direction.DOWN);
        }
        if (xdiff > 0.5) {
            dirs.add(Direction.EAST);
        } else if (xdiff < -0.5) {
            dirs.add(Direction.WEST);
        } else {
            dirs.add(Direction.EAST);
            dirs.add(Direction.WEST);
        }
        if (zdiff > 0.5) {
            dirs.add(Direction.SOUTH);
        } else if (zdiff < -0.5) {
            dirs.add(Direction.NORTH);
        } else {
            dirs.add(Direction.SOUTH);
            dirs.add(Direction.NORTH);
        }
        return dirs;
    }
}