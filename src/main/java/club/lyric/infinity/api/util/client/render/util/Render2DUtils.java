package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

/**
 * @author vasler, railhack
 * visual util
 */
@SuppressWarnings({"unused"})

public class Render2DUtils implements IMinecraft {
    /**
     * @param matrices - amount of arrays used in the rectangle (context.getMatrices())
     * @param x        - location of the rectangle on the x-axis
     * @param y        - location of the rectangle on the y-axis
     * @param width    - the horizontal measurement of the rectangle from angle to angle
     * @param height   - the vertical measurement of the rectangle from angle to angle
     * @param color    - color of the rectangle in RGB
     */
    public static void drawRect(MatrixStack matrices, float x, float y, float width, float height, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // Colors
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

        // Rect starts here
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(r, g, b, a);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        end();
        // Rect ends here
    }

    /**
     * @param matrices - amount of arrays used in the rectangle (context.getMatrices())
     * @param x        - location of the rectangle on the x-axis
     * @param y        - location of the rectangle on the y-axis
     * @param width    - the horizontal measurement of the rectangle from angle to angle
     * @param height   - the vertical measurement of the rectangle from angle to angle
     * @param color    - color of the rectangle in RGB
     */
    public static void drawOutlineRect(MatrixStack matrices, float x, float y, float width, float height, float lineWidth, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // Colors
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

        RenderSystem.lineWidth(lineWidth);

        // Rect starts here
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        setup();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrix, x, y, 0.0F).color(r, g, b, a);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        end();
        // Rect ends here

        RenderSystem.lineWidth(1.0F);
    }

    public static void drawGradient(MatrixStack matrixStack, float x, float y, float x2, float y2, int startColor, int endColor, boolean sideways) {

        float[] startRGBA = {

                (startColor >> 16 & 255) / 255.0F,
                (startColor >> 8 & 255) / 255.0F,
                (startColor & 255) / 255.0F,

                (startColor >> 24 & 255) / 255.0F
        };
        float[] endRGBA = {

                (endColor >> 16 & 255) / 255.0F,
                (endColor >> 8 & 255) / 255.0F,
                (endColor & 255) / 255.0F,

                (endColor >> 24 & 255) / 255.0F
        };

        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        if (sideways) {
            bufferBuilder.vertex(posMatrix, x, y, 0.0F).color(startRGBA[0], startRGBA[1], startRGBA[2], startRGBA[3]);
            bufferBuilder.vertex(posMatrix, x, y2, 0.0F).color(startRGBA[0], startRGBA[1], startRGBA[2], startRGBA[3]);
            bufferBuilder.vertex(posMatrix, x2, y2, 0.0F).color(endRGBA[0], endRGBA[1], endRGBA[2], endRGBA[3]);
            bufferBuilder.vertex(posMatrix, x2, y, 0.0F).color(endRGBA[0], endRGBA[1], endRGBA[2], endRGBA[3]);
        } else {
            bufferBuilder.vertex(posMatrix, x2, y, 0.0F).color(startRGBA[0], startRGBA[1], startRGBA[2], startRGBA[3]);
            bufferBuilder.vertex(posMatrix, x, y, 0.0F).color(startRGBA[0], startRGBA[1], startRGBA[2], startRGBA[3]);
            bufferBuilder.vertex(posMatrix, x, y2, 0.0F).color(endRGBA[0], endRGBA[1], endRGBA[2], endRGBA[3]);
            bufferBuilder.vertex(posMatrix, x2, y2, 0.0F).color(endRGBA[0], endRGBA[1], endRGBA[2], endRGBA[3]);
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
    }


    public static void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void end() {
        RenderSystem.disableBlend();
    }
}
