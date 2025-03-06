package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.render.util.InterpolationUtils;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/**
 * @author lyric
 * this allows manual air placement, block air placement has been implemented into BlockModuleBase
 */
public final class AirPlace extends ModuleBase
{
    public BooleanSetting render = new BooleanSetting("Render", true, this);

    public AirPlace()
    {
        super("AirPlace", "aa", Category.PLAYER);
    }

    @Override
    public void onUpdate()
    {
        BlockHitResult result = checkIfValid();
        if (mc.options.useKey.isPressed() && result != null)
        {
            place(result);
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (checkIfValid() == null) return;
        Box inter = InterpolationUtils.interpolatePos(checkIfValid().getBlockPos(), 1.0f);
        Render3DUtils.enable3D();
        matrixStack.push();
        Render3DUtils.drawOutline(matrixStack, inter, Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB());
        matrixStack.pop();
        Render3DUtils.disable3D();
    }

    private void place(BlockHitResult result)
    {
        ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result);
        if (actionResult.isAccepted())
        {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    /**
     * runs our initial check to see if we can air place.
     */
    private BlockHitResult checkIfValid()
    {
        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return null;

        BlockPos blockPos = result.getBlockPos();

        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem)) return null;

        if (!(mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR || mc.world.getBlockState(blockPos).getBlock() == Blocks.LAVA || mc.world.getBlockState(blockPos).getBlock() == Blocks.WATER)) return null;
        return result;
    }
}
