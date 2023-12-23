package club.lyric.infinity.api.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * @author vasler
 * render util
 */
public class Render2DUtils
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


    public static void disableStandardItemLighting()
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glDisable(GL11.GL_LIGHT1);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
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
