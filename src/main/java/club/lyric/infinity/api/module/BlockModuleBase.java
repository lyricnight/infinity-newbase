package club.lyric.infinity.api.module;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.api.util.minecraft.rotation.RotationHandler;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author lyric
 * unfinished
 */
//TODO: implement block placements properly, and update this to make it work properly -> maybe switch to some sort of BlockPlacer service to handle placement queues?
public class BlockModuleBase extends ModuleBase {
    /**
     * I could simplify this so much if I got rid of normal who even uses that shit
     * isn't in AntiCheat because this only makes sense on a module-by-module basis
     */
    public ModeSetting swapModeSetting = new ModeSetting("SwapMode", this, "Normal", "Normal", "Silent", "Slot");

    /**
     * isn't in AntiCheat because this only makes sense on a module-by-module basis
     */
    public BooleanSetting airPlace = new BooleanSetting("AirPlace", false, this);

    /**
     * whether we should render blocks placed.
     */
    public BooleanSetting render = new BooleanSetting("Render", false, this);

    /**
     * colors for rendering blocks placed
     */
    public ColorSetting colorVal = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), true);
    /**
     * again we want this on a module-by-module basis.
     */
    public BooleanSetting attack = new BooleanSetting("Attack", false, this);
    /**
     * represents priority for rotations and block placements
     */
    private final int prio;

    /**
     * represents slot for switchBack
     */
    private int slot;

    /**
     * represents positions where we have placed blocks
     */
    private final List<BlockPos> renderingPos = new ArrayList<>();

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
    protected boolean canPlace()
    {
        return getPlaceableBlockSlot() != -1;
    }

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

    protected void setRotationPoint(float yaw, float pitch, boolean instant)
    {
        Managers.ROTATIONS.setRotationPoint(new RotationPoint(yaw, pitch, getPriority(), instant));
    }

    protected boolean blocked() {
        return Managers.ROTATIONS.isRotationLate(getPriority());
    }

    public int getPriority() {
        return prio;
    }

    /**
     * this is the method you call to place a block
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
    protected boolean placeBlock(BlockPos pos, RotationHandler rotationHandler)
    {
        Direction direction = getDirection(pos, airPlace.value());
        if (direction == null && !Managers.ANTICHEAT.isStrictDirection())
        {
            Infinity.LOGGER.error("Logged no direction!");
            Infinity.LOGGER.error("Resetting to default: (Direction.UP)");
            Infinity.LOGGER.error("Report this!");
            direction = Direction.UP;
        }
        if (direction == null)
        {
            return false;
        }
        BlockPos neighbor = pos.offset(direction.getOpposite());
        return placeBlock(neighbor, direction, rotationHandler);
    }

    private boolean placeBlock(BlockPos pos, Direction direction, RotationHandler rotationHandler)
    {
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d(direction.getUnitVector()).multiply(0.5));
        return placeBlock(new BlockHitResult(hitVec, direction, pos, false), rotationHandler);
    }

    private boolean placeBlock(BlockHitResult hitResult, RotationHandler rotationHandler)
    {
        boolean willSwap = getPlaceableBlockSlot() != Managers.INVENTORY.getSlot();
        if (willSwap)
        {
            slot = Managers.INVENTORY.getClientSlot();
            swapToBlock(getModeFromString(swapModeSetting.getMode()));
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

        if (willSwap)
        {
            switch (getModeFromString(swapModeSetting.getMode()))
            {
                case Normal -> swapToItem(slot, SwapMode.Normal);
                case Silent, Slot -> Managers.INVENTORY.forceSync();
            }
        }

        return result;
    }

    private boolean place(BlockHitResult result)
    {
        renderingPos.add(result.getBlockPos());

        ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result);
        if (actionResult.isAccepted() && actionResult.shouldSwingHand())
        {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
        return actionResult.isAccepted();
    }

    /**
     * gets direction for block placement.
     */
    private Direction getDirection(BlockPos blockPos, boolean airPlace)
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
            if (Managers.ANTICHEAT.isStrictDirection() && !validDirectionsSet.contains(direction.getOpposite()))
            {
                continue;
            }
            interactDirection = direction;
            break;
        }
        if (interactDirection == null && airPlace)
        {
            return Direction.UP;
        }
        else if (interactDirection == null) return null;
        return airPlace ? Direction.UP : interactDirection.getOpposite();
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

    /**
     * swapping methods
     */
    //TODO: slot
    protected void swapToBlock(SwapMode mode)
    {
        switch (mode) {
            case Normal -> Managers.INVENTORY.setSlotFull(getPlaceableBlockSlot());
            case Silent, Slot -> Managers.INVENTORY.setSlotPacket(getPlaceableBlockSlot());
        }
    }

    protected void swapToItem(int slot, SwapMode mode)
    {
        switch (mode)
        {
            case Normal -> Managers.INVENTORY.setSlotFull(slot);
            case Silent, Slot -> Managers.INVENTORY.setSlotPacket(slot);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {

        if (Null.is() || !render.value()) return;

        for (BlockPos render : renderingPos)
        {
            Color colors = colorVal.getColor();

            Box bb = Interpolation.interpolatePos(render, 1.0f);

            Render3DUtils.enable3D();
            matrixStack.push();

            Render3DUtils.drawBox(matrixStack, bb, new Color(colors.getRed(), colors.getGreen(), colors.getBlue(), 73).getRGB());

            Render3DUtils.drawOutline(matrixStack, bb, new Color(colors.getRed(), colors.getGreen(), colors.getBlue(), 255).getRGB());

            matrixStack.pop();
            Render3DUtils.disable3D();
        }
    }

    /**
     * @vasler put this in renderutils.
     * @param value
     * @param max
     * @return
     */
    public static double fade(double value, double max)
    {
        double elapsedTime = System.currentTimeMillis() - value;

        double fadeAmount = MathUtils.normalize(elapsedTime, max);

        int alpha = (int) (fadeAmount * 255.0);
        alpha = MathHelper.clamp(alpha, 0, 255);

        return 255 - alpha;
    }
}