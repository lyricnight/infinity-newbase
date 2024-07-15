package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;

/**
 * @author vasler
 */
public class BlockHighlight extends ModuleBase {
    public ColorSetting lineColor = new ColorSetting("LineColor", this, new JColor(new Color(50, 255, 50, 255)));
    public ColorSetting boxColor = new ColorSetting("BoxColor", this, new JColor(new Color(50, 255, 50, 255)));
    public BooleanSetting outline = new BooleanSetting("Outline", true, this);
    public BooleanSetting box = new BooleanSetting("Box", true, this);

    public BlockHighlight() {
        super("BlockHighlight", "hhh", Category.Visual);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {

        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;

        BlockPos blockPos = result.getBlockPos();
        Box inter = Interpolation.interpolatePos(blockPos, 1.0f);

        Block block = mc.world.getBlockState(blockPos).getBlock();

        if (block instanceof AirBlock || block instanceof FluidBlock) return;

        Render3DUtils.enable3D();
        matrixStack.push();

        if (box.value())
            Render3DUtils.drawBox(matrixStack, inter, new Color(boxColor.getColor().getRed(), boxColor.getColor().getGreen(), boxColor.getColor().getBlue(), boxColor.getColor().getAlpha()).getRGB());

        if (outline.value())
            Render3DUtils.drawOutline(matrixStack, inter, new Color(lineColor.getColor().getRed(), lineColor.getColor().getGreen(), lineColor.getColor().getBlue(), lineColor.getColor().getAlpha()).getRGB());

        matrixStack.pop();
        Render3DUtils.disable3D();
    }
}
