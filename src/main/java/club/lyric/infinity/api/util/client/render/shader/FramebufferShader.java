package club.lyric.infinity.api.util.client.render.shader;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL20;

import java.awt.*;

/**
 * old infinity
 */
public abstract class FramebufferShader extends Shader implements IMinecraft {

    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;
    protected float radius;
    protected float quality;
    protected float saturation;
    protected float speed;
    protected float x, y;
    private SimpleFramebuffer framebuffer;
    private boolean entityShadows;

    public FramebufferShader(final String fragmentShader) {
        super(fragmentShader);
        alpha = 1.0f;
        radius = 2.0f;
        quality = 1.0f;
    }

    public void startDraw(final float partialTicks) {

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();

        (framebuffer = (SimpleFramebuffer) setupFrameBuffer(framebuffer)).clear(MinecraftClient.IS_SYSTEM_MAC);
        framebuffer.beginWrite(true);
        entityShadows = mc.options.getEntityShadows().getValue();
        mc.options.getEntityShadows().setValue(false);
        mc.gameRenderer.renderWorld(partialTicks, 0, new MatrixStack());
    }

    public void stopDraw(final Color color, final float radius, final float quality, float saturation, float speed, float x, float y) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        mc.options.getEntityShadows().setValue(entityShadows);
        framebuffer.endWrite();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.getFramebuffer().beginWrite(true);
        this.saturation = saturation;
        this.speed = speed;
        this.x = x;
        this.y = y;
        red = color.getRed() / 255.0f;
        green = color.getGreen() / 255.0f;
        blue = color.getBlue() / 255.0f;
        alpha = color.getAlpha() / 255.0f;
        this.radius = radius;
        this.quality = quality;
        startShader();
        mc.gameRenderer.getOverlayTexture();
        drawFramebuffer(framebuffer);
        stopShader();
        matrixStack.pop();
    }

    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (frameBuffer == null) {
            return new SimpleFramebuffer(mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        }
        if (frameBuffer.textureWidth != mc.getFramebuffer().textureWidth || frameBuffer.textureHeight != mc.getFramebuffer().textureHeight) {
            frameBuffer.delete();
            frameBuffer = new SimpleFramebuffer(mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        }
        return frameBuffer;
    }

    public void drawFramebuffer(final Framebuffer framebuffer) {
        final int screenWidth = mc.getWindow().getScaledWidth();
        final int screenHeight = mc.getWindow().getScaledHeight();
        RenderSystem.bindTexture(framebuffer.getColorAttachment());

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(0.0D, screenHeight, 0.0D).texture(0.0F, 1.0F).next();
        buffer.vertex(screenWidth, screenHeight, 0.0D).texture(1.0F, 1.0F).next();
        buffer.vertex(screenWidth, 0.0D, 0.0D).texture(1.0F, 0.0F).next();
        buffer.vertex(0.0D, 0.0D, 0.0D).texture(0.0F, 0.0F).next();
        Tessellator.getInstance().draw();

        GL20.glUseProgram(0);
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }
}