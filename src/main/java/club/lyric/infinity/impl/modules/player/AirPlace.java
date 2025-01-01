package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
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
 */
public final class AirPlace extends ModuleBase
{
    public BooleanSetting render = new BooleanSetting("Render", true, this);

    public AirPlace()
    {
        super("AirPlace", "aa", Category.Player);
    }

    @Override
    public void onUpdate()
    {
        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

        BlockPos blockPos = result.getBlockPos();

        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem)) return;

        if (!(mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR || mc.world.getBlockState(blockPos).getBlock() == Blocks.LAVA || mc.world.getBlockState(blockPos).getBlock() == Blocks.WATER)) return;

        if (mc.options.useKey.isPressed())
        {
            place(result);
        }

    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {

        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

        BlockPos blockPos = result.getBlockPos();

        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem)) return;

        if (!(mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR || mc.world.getBlockState(blockPos).getBlock() == Blocks.LAVA || mc.world.getBlockState(blockPos).getBlock() == Blocks.WATER)) return;

        Box inter = Interpolation.interpolatePos(blockPos, 1.0f);

        Render3DUtils.enable3D();
        matrixStack.push();

        Render3DUtils.drawOutline(matrixStack, inter, Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB());

        matrixStack.pop();
        Render3DUtils.disable3D();
    }

    private void place(BlockHitResult result)
    {
        ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result);;
        if (actionResult.isAccepted())
        {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
