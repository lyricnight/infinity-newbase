package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.render.Render3DEvent;
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
public class HoleESP extends ModuleBase {

    // Properties
    public NumberSetting range = new NumberSetting("Range", this, 4f, 1f, 10f, 0.5f);
    public NumberSetting size = new NumberSetting("Size", this, 1f, 0.01f, 1f, 0.5f);

    // Color
    public ColorSetting bedrock = new ColorSetting("Bedrock", this, new JColor(new Color(50, 255, 50, 76)), true);
    public ColorSetting obsidian = new ColorSetting("Obsidian", this, new JColor(new Color(255, 50, 50, 76)), true);

    // Box
    public BooleanSetting doubles = new BooleanSetting("Doubles", true, this);
    public BooleanSetting outline = new BooleanSetting("Outline", true, this);
    public BooleanSetting box = new BooleanSetting("Box", true, this);

    // Misc
    public BooleanSetting onlyOut = new BooleanSetting("OnlyOut", false, this);
    public BooleanSetting fade = new BooleanSetting("Fade", false, this);



    protected List<Hole> holes = new ArrayList<>();
    ExecutorService service = Executors.newCachedThreadPool();

    public HoleESP() {
        super("HoleESP", "Esps holes", Category.Render);
    }

    @Override
    public void onUpdate() {
        service.submit(() -> {
            holes = HoleUtils.getHoles(mc.player, range.getFValue(), doubles.value(), false, false, false);
        });
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (holes.isEmpty()) {
            return;
        }

        MatrixStack matrix = event.getMatrix();

        if (onlyOut.value() && HoleUtils.isInHole(mc.player))
        {
            return;
        }

        for (Hole hole : holes) {
            double alpha = 1.0;
            double outlineAlpha;

            if (fade.value()) {
                double fadeRange = range.getValue() - 1.0;
                double fadeRangeSq = fadeRange * fadeRange;
                alpha = (fadeRangeSq + 9.0 - mc.player.squaredDistanceTo(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) / fadeRangeSq;
                alpha = MathHelper.clamp(alpha, 0.0, 1.0);
            }

            Color color = HoleUtils.isBedrockHole(new BlockPos(hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ())) ? bedrock.getColor() : obsidian.getColor();

            alpha = alpha * color.getAlpha();
            outlineAlpha = alpha * 255;

            Box bb = interpolatePos(hole.getFirst(), size.getFValue());

            if (hole.getSecond() != null) {
                bb = new Box(hole.getFirst().getX() - getCameraPos().x, hole.getFirst().getY() - getCameraPos().y, hole.getFirst().getZ() - getCameraPos().z, hole.getSecond().getX() + 1 - getCameraPos().x, hole.getSecond().getY() + size.getFValue() - getCameraPos().y, hole.getSecond().getZ() + 1 - getCameraPos().z);
            }

            // idk why the fuck it changes the alpha of the screen
            Render3DUtils.enable3D();
            matrix.push();

            if (box.value()) {
                Render3DUtils.drawBox(event.getMatrix(), bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha).getRGB());
            }

            if (outline.value())
            {
                Render3DUtils.drawOutline(event.getMatrix(), bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) outlineAlpha).getRGB());
            }

            matrix.pop();
            Render3DUtils.disable3D();

        }
    }

    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }

    public static Box interpolatePos(BlockPos pos, float size) {
        return new Box(
                pos.getX() - getCameraPos().x,
                pos.getY() - getCameraPos().y,
                pos.getZ() - getCameraPos().z,
                pos.getX() - getCameraPos().x + 1,
                pos.getY() - getCameraPos().y + size,
                pos.getZ() - getCameraPos().z + 1
        );
    }

}
