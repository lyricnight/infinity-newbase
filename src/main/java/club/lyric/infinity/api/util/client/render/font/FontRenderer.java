package club.lyric.infinity.api.util.client.render.font;

import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.binary.Base64;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// skidded from Prestige clint
public class FontRenderer {
    private final float fontSize;
    private final int startChar;
    private final int endChar;
    private final float[] xPos;
    private final float[] yPos;
    private Font font;
    private Graphics2D graphics;
    private FontMetrics metrics;
    private BufferedImage bufferedImage;
    private Identifier resourceLocation;

    public FontRenderer(InputStream font) {
        this(font, 18F);
    }

    public FontRenderer(InputStream font, float size) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        setupGraphics2D();
        createFont(font, size);
    }

    private static NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return NativeImage.read(bais);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private void setupGraphics2D() {
        bufferedImage = new BufferedImage(256, 256, 2);
        graphics = ((Graphics2D) bufferedImage.getGraphics());
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }

    private void createFont(InputStream font, float size) {
        try {
            this.font = Font.createFont(0, font).deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphics.setFont(this.font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, 256, 256);
        graphics.setColor(Color.white);
        metrics = graphics.getFontMetrics();

        float x = 5.0F;
        float y = 5.0F;
        for (int i = startChar; i < endChar; i++) {
            graphics.drawString(Character.toString((char) i), x, y + metrics.getAscent());
            xPos[(i - startChar)] = x;
            yPos[(i - startChar)] = (y - metrics.getMaxDescent());
            x += metrics.stringWidth(Character.toString((char) i)) + 2.0F;
            if (x >= 250 - metrics.getMaxAdvance()) {
                x = 5.0F;
                y += metrics.getMaxAscent() + metrics.getMaxDescent() + fontSize / 2.0F;
            }
        }
        String base64 = imageToBase64String(bufferedImage);
        setResourceLocation(base64);
    }

    private String imageToBase64String(BufferedImage image) {
        String ret;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace(System.lineSeparator(), "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    public void setResourceLocation(String base64) {
        NativeImage image = readTexture(base64);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }

        image.close();
        resourceLocation = new Identifier("prestige", "font/font.ttf");
        applyTexture(resourceLocation, imgNew);
    }

    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(nativeImage)));
    }

    public void drawString(MatrixStack matrixStack, String text, float x, float y, Color color, boolean shadow) {
        matrixStack.push();
        RenderSystem.depthFunc(519);
        RenderSystem.enableBlend();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        if (shadow) {
            drawer(matrixStack, text, x + 0.5f, y + 0.5f, ColorUtils.darken(color, 187));
        }
        drawer(matrixStack, text, x, y, color);
        matrixStack.scale(1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public void drawString(String text, float x, float y, Color color) {
        drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, Color color, boolean shadow) {
        MatrixStack matrixStack = Managers.TEXT.getMatrixStack();
        drawString(matrixStack, text, x, y, color, shadow);
    }

    private void drawer(MatrixStack matrixStack, String text, float x, float y, Color color) {
        StringBuilder finalText = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= startChar && c <= endChar) finalText.append(c);
            else finalText.append("?");
        }
        text = finalText.toString();
        x *= 2.0F;
        y *= 2.0F;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, resourceLocation);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShaderColor((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        for (int i = 0; i < text.length(); i++) {
            try {
                char c = text.charAt(i);
                drawChar(matrixStack, c, x, y);
                x += getStringWidth(Character.toString(c)) * 2.0F;
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
        //bufferBuilder.end();
        tessellator.draw();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public final float getStringWidth(String text) {
        return (float) (getBounds(text).getWidth()) / 2.0F;
    }

    public float getStringHeight() {
        return (float)getBounds("W").getHeight() / 2.0f;
    }

    private Rectangle2D getBounds(String text) {
        return metrics.getStringBounds(text, graphics);
    }

    private void drawChar(MatrixStack matrixStack, char character, float x, float y) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = metrics.getStringBounds(Character.toString(character), graphics);
        drawTexturedModalRect(matrixStack, x, y, xPos[(character - startChar)], yPos[(character - startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + metrics.getMaxDescent() + 1.0F);
    }

    private void drawTexturedModalRect(MatrixStack matrixStack, float x, float y, float u, float v, float width, float height) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float scale = 0.0039063F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + height, 0.0f).texture((u + 0.0F) * scale, (v + height) * scale).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0.0f).texture((u + width) * scale, (v + height) * scale).next();
        bufferBuilder.vertex(matrix4f, x + width, y + 0.0F, 0.0f).texture((u + width) * scale, (v + 0.0F) * scale).next();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + 0.0F, 0.0f).texture((u + 0.0F) * scale, (v + 0.0F) * scale).next();
    }
}
