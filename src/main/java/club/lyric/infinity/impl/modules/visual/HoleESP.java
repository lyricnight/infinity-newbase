package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.block.hole.Hole;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author vasler
 */
public final class HoleESP extends ModuleBase {

    // Properties
    public NumberSetting range = new NumberSetting("Range", this, 4f, 1f, 30f, 1f, "m");
    public NumberSetting size = new NumberSetting("Size", this, 1f, 0.01f, 1f, 0.1f);

    // Color
    public ColorSetting bedrock = new ColorSetting("Bedrock", this, new JColor(new Color(50, 255, 50)), false);
    public ColorSetting obsidian = new ColorSetting("Obsidian", this, new JColor(new Color(255, 50, 50)), false);
    public ColorSetting doubles = new ColorSetting("Doubles", this, new JColor(new Color(50, 50, 255)), false);

    // Box
    public BooleanSetting doublez = new BooleanSetting("Doubles", true, this);
    public BooleanSetting outline = new BooleanSetting("Outline", true, this);
    public BooleanSetting box = new BooleanSetting("Box", true, this);

    // MISC
    public BooleanSetting fade = new BooleanSetting("Fade", false, this);


    private List<Hole> holes = new ArrayList<>();
    ExecutorService service = Executors.newCachedThreadPool();

    public HoleESP() {
        super("HoleESP", "Esps holes", Category.VISUAL);
    }

    @Override
    public void onUpdate() {
        service.submit(() -> {
            holes = HoleUtils.getHoles(mc.player, range.getFValue(), doublez.value(), false, false, false);
        });
    }

    @Override
    public String moduleInformation() {
        return holes.size() + "";
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (holes.isEmpty()) return;

        for (Hole hole : holes) {
            double alpha = 1.0;
            double outlineAlpha = 1.0;

            if (fade.value()) {
                double fadeRange = range.getValue() - 1.0;
                double fadeRangeSq = fadeRange * fadeRange;
                alpha = (fadeRangeSq + 9.0 - mc.player.squaredDistanceTo(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) / fadeRangeSq;
                alpha = MathHelper.clamp(alpha, 0.0, 1.0);
                outlineAlpha = (fadeRangeSq + 9.0 - mc.player.squaredDistanceTo(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) / fadeRangeSq;
                outlineAlpha = MathHelper.clamp(outlineAlpha, 0.0, 1.0);
            }

            Color color = HoleUtils.isBedrockHole(new BlockPos(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) ? bedrock.getColor() : HoleUtils.isDoubleHole(new BlockPos(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) ? doubles.getColor() : obsidian.getColor();

            alpha = alpha * 73;
            outlineAlpha = outlineAlpha * 255;

            Box bb = interpolatePos(hole.getFirst(), size.getFValue());

            if (hole.getSecond() != null)
                bb = new Box(hole.getFirst().getX() - getCameraPos().x, hole.getFirst().getY() - getCameraPos().y, hole.getFirst().getZ() - getCameraPos().z, hole.getSecond().getX() + 1 - getCameraPos().x, hole.getSecond().getY() + size.getFValue() - getCameraPos().y, hole.getSecond().getZ() + 1 - getCameraPos().z);

            Render3DUtils.enable3D();
            matrixStack.push();

            if (box.value())
                Render3DUtils.drawBox(matrixStack, bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha).getRGB());

            if (outline.value())
                Render3DUtils.drawOutline(matrixStack, bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) outlineAlpha).getRGB());

            matrixStack.pop();
            Render3DUtils.disable3D();

        }
    }

    private static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }

    private static Box interpolatePos(BlockPos pos, float size) {
        return new Box(pos.getX() - getCameraPos().x, pos.getY() - getCameraPos().y, pos.getZ() - getCameraPos().z, pos.getX() - getCameraPos().x + 1, pos.getY() - getCameraPos().y + size, pos.getZ() - getCameraPos().z + 1);
    }

}
