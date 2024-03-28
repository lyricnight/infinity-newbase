package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

/**
 * @author vasler, railhack
 * render util
 */
@SuppressWarnings("unused")

public class Render2DUtils implements IMinecraft
{

    /**
     * @param matrices - amount of arrays used in the rectangle (context.getMatrices())
     * @param x - location of the rectangle on the x-axis
     * @param y - location of the rectangle on the y-axis
     * @param width - the horizontal measurement of the rectangle from angle to angle
     * @param height - the vertical measurement of the rectangle from angle to angle
     * @param color - color of the rectangle in RGB
     */
    public static void drawRect(MatrixStack matrices, float x, float y, float width, float height, int color)
    {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // Colors
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

        // Rect starts here
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(r, g, b, a).next();
        Tessellator.getInstance().draw();
        end();
        // Rect ends here
    }

    public static void drawOutlineRect(MatrixStack matrices, float x, float y, float width, float height, float lineSize, int color) {
        drawRect(matrices, x, y, x + lineSize, height, color);
        drawRect(matrices, width - lineSize, y, width, height, color);
        drawRect(matrices, x, height - lineSize, width, height, color);
        drawRect(matrices, x, y, width, y + lineSize, color);
    }

    public static void setup()
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void end()
    {
        RenderSystem.disableBlend();
    }
}
