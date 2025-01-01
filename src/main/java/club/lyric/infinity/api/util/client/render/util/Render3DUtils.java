package club.lyric.infinity.api.util.client.render.util;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Render3DUtils implements IMinecraft {

    public static void enable3D() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
    }

    public static void disable3D() {
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    public static void resetColor() {
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    // swish pasted rewrite
    public static void drawBox(MatrixStack matrixStack, Box bb, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        RenderSystem.setShader(ShaderProgramKeys.POSITION);

        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);

        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        resetColor();
    }

    public static void drawOutline(MatrixStack matrixStack, Box bb, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        RenderSystem.setShader(ShaderProgramKeys.POSITION);

        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);

        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.minY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.minZ);
        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);

        bufferBuilder.vertex(matrix, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.maxZ);
        bufferBuilder.vertex(matrix, (float) bb.minX, (float) bb.maxY, (float) bb.minZ);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        resetColor();
    }
}
