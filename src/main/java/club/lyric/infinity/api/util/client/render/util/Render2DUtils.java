package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author vasler, railhack
 * render util
 */
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

    public static void setup()
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void end()
    {
        RenderSystem.disableBlend();
    }

    public static void renderRect(Rect rect, Color color) {
        RenderSystem.enableBlend();
        ColorUtils.glColor(color);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBegin(GL_QUADS);
        GL11.glVertex2f(rect.getX(), rect.getY());
        GL11.glVertex2f(rect.getX(), rect.getY() + rect.getHeight());
        GL11.glVertex2f(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        GL11.glVertex2f(rect.getX() + rect.getWidth(), rect.getY());
        GL11.glEnd();
        GL11.glEnable(GL_TEXTURE_2D);
    }

    public static void renderRectRollingRainbow(final Rect rect, final int alpha) {
        float hueSpeedX = 0.005f;
        float hueSpeedY = 0.005f;

        Color hueTopLeft = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX(), rect.getY(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), alpha);
        Color hueTopRight = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX() + rect.getWidth(), rect.getY(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), alpha);
        Color hueBottomLeft = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX(), rect.getY() + rect.getHeight(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), alpha);
        Color hueBottomRight = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), alpha);
        renderRectGradient(rect, hueTopRight, hueTopLeft, hueBottomLeft, hueBottomRight);
    }

    public static void renderRectRollingRainbow(final Rect rect, final int bottomAlpha, final int topAlpha) {
        float hueSpeedX = 0.005f;
        float hueSpeedY = 0.005f;

        Color hueTopLeft = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX(), rect.getY(), hueSpeedX, hueSpeedY), 0f, 0f), topAlpha);
        Color hueTopRight = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX() + rect.getWidth(), rect.getY(), hueSpeedX, hueSpeedY), 0f, 0f), topAlpha);
        Color hueBottomLeft = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX(), rect.getY() + rect.getHeight(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), bottomAlpha);
        Color hueBottomRight = ColorUtils.newAlpha(Color.getHSBColor(MathUtils.calculateHue(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), hueSpeedX, hueSpeedY), 1.0f, 1.0f), bottomAlpha);
        renderRectGradient(rect, hueTopRight, hueTopLeft, hueBottomLeft, hueBottomRight);
    }

    public static void renderRectGradient(Rect rect, Color topRight, Color topLeft, Color bottomLeft, Color bottomRight) {
        RenderSystem.enableBlend();
        GL11.glShadeModel(GL_SMOOTH);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBegin(GL_QUADS);
        ColorUtils.glColor(topLeft);
        GL11.glVertex2f(rect.getX(), rect.getY());
        ColorUtils.glColor(bottomLeft);
        GL11.glVertex2f(rect.getX(), rect.getY() + rect.getHeight());
        ColorUtils.glColor(bottomRight);
        GL11.glVertex2f(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        ColorUtils.glColor(topRight);
        GL11.glVertex2f(rect.getX() + rect.getWidth(), rect.getY());
        GL11.glEnd();
        GL11.glEnable(GL_TEXTURE_2D);
    }

    public static void renderRectOutline(Rect rect, Color color, double linWid) {
        GlStateManager.func_179147_l();
        GL11.glLineWidth((float)linWid);
        GL11.glDisable(2848);
        ColorUtil.glColor(color);
        GL11.glDisable(3553);
        GL11.glBegin(2);
        GL11.glVertex2i(rect.getX(), rect.getY());
        GL11.glVertex2i(rect.getX(), rect.getY() + rect.getHeight());
        GL11.glVertex2i(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        GL11.glVertex2i(rect.getX() + rect.getWidth(), rect.getY());
        GL11.glEnd();
        GL11.glEnable(3553);
    }
}
