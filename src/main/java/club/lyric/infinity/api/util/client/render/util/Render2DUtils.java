package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.client.render.shader.Shader;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

/**
 * @author vasler, railhack
 * visual util
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

    public static void quadsBegin(MatrixStack matrices, float x, float y, float width, float height) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Tessellator tessellator = Tessellator.getInstance();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        {
            bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y, 0).texture(0, 0).next();
            bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y + height, 0).texture(0, 1).next();
            bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y + height, 0).texture(1, 1).next();
            bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y, 0).texture(1, 0).next();
        }
        tessellator.draw();
    }


    public static void drawOutlineRect(MatrixStack matrices, float x, float y, float width, float height, float lineSize, int color) {
        drawRect(matrices, x, y, x + lineSize, height, color);
        drawRect(matrices, width - lineSize, y, width, height, color);
        drawRect(matrices, x, height - lineSize, width, height, color);
        drawRect(matrices, x, y, width, y + lineSize, color);
    }

    public static void drawRoundedRect(MatrixStack matrices, float x, float y, float width, float height, float radius, int color) {
        matrices.push();
        RenderSystem.enableBlend();
        Shader.ROUND_SHADER.attach();
        Shader.setupRoundedRectUniforms(x, y, width, height, radius, Shader.ROUND_SHADER);
        Shader.ROUND_SHADER.setUniform("blur", 0);
        Shader.ROUND_SHADER.setUniform("color", getRed(color) / 255f, getGreen(color) / 255f, getBlue(color) / 255f, getAlpha(color) / 255f);
        Shader.ROUND_SHADER.drawQuads(matrices, x, y, width, height);
        Shader.ROUND_SHADER.detach();
        RenderSystem.disableBlend();
        matrices.pop();
    }

    public static int getRed(final int hex) {
        return hex >> 16 & 255;
    }

    public static int getGreen(final int hex) {
        return hex >> 8 & 255;
    }

    public static int getBlue(final int hex) {
        return hex & 255;
    }

    public static int getAlpha(final int hex) {
        return hex >> 24 & 255;
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
