package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.nanovg.NanoVGInitializer;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.nanovg.NVGColor;

import java.awt.*;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author vasler, railhack
 * visual util
 */
@SuppressWarnings("unused")

public class Render2DUtils implements IMinecraft
{

    @Getter
    private static Render2DUtils instance;
    public Render2DUtils()
    {
        instance = this;
    }
    public void init()
    {
        NanoVGInitializer.init();
    }

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

    public static void drawRoundedRect(long context, float x, float y, float w, float h, float radius, int color)
    {
        nvgBeginPath(context);
        nvgRoundedRect(context, x, y, w, h,radius);
        Color color2 = new Color(color);
        NVGColor nvgColor = nvgColor(color2);
        nvgFillColor(context, nvgColor);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static NVGColor nvgColor(Color color)
    {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255.0f);
        nvgColor.g(color.getGreen() / 255.0f);
        nvgColor.b(color.getBlue() / 255.0f);
        nvgColor.a(color.getAlpha() / 255.0f);
        return nvgColor;
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
