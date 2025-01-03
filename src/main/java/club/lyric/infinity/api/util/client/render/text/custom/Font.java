package club.lyric.infinity.api.util.client.render.text.custom;


import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13C;

import java.io.InputStream;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public class Font implements IMinecraft {
    private float posX, posY;
    private final int[] colorCode = new int[32];
    private final GlyphPage regularGlyphPage;

    public Font(GlyphPage regularGlyphPage) {
        this.regularGlyphPage = regularGlyphPage;

        for (int i = 0; i < 32; ++i) {
            int red = (i >> 3 & 1) * 85;
            int green = (i >> 2 & 1) * 170 + red;
            int blue = (i >> 1 & 1) * 170 + red;
            int color = (i & 1) * 170 + red;

            if (i == 6) {
                green += 85;
            }

            if (i >= 16) {
                green /= 4;
                blue /= 4;
                color /= 4;
            }

            this.colorCode[i] = (green & 255) << 16 | (blue & 255) << 8 | color & 255;
        }
    }

    public void draw(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x, y, color, false);
    }

    public void draw(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) x, (float) y, color, false);
    }

    public void drawRight(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x - (getWidth(text)), y, color, false);
    }

    public void drawRight(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) (x - (getWidth(text))), (float) y, color, false);
    }

    public void drawShadow(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x, y, color, true);
    }

    public void drawShadow(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) x, (float) y, color, true);
    }

    public void drawRightShadow(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x - (getWidth(text)), y, color, true);
    }

    public void drawRightShadow(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) (x - (getWidth(text))), (float) y, color, true);
    }

    public void drawCenter(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x - getWidth(text) / 2f, y, color, false);
    }

    public void drawCenter(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) x - getWidth(text) / 2f, (float) y, color, false);
    }

    public void drawCenterShadow(MatrixStack matrix, String text, float x, float y, int color) {
        draw(matrix, text, x - getWidth(text) / 2f, y, color, true);
    }

    public void drawCenterShadow(MatrixStack matrix, String text, double x, double y, int color) {
        draw(matrix, text, (float) x - getWidth(text) / 2f, (float) y, color, true);
    }

    public void drawOutline(MatrixStack matrix, String text, double x, double y, int color) {
        drawOutline(matrix, text, x, y, color, 0.25F);
    }

    public void drawOutline(MatrixStack matrix, String text, double x, double y, int color, float multDark) {
        float outline = 0.5F;
        int alpha = ColorUtils.alpha(color);
        int outlineColor = ColorUtils.replAlpha(ColorUtils.multDark(color, multDark), alpha);
        draw(matrix, stripControlCodes(text), x, y + outline, outlineColor);
        draw(matrix, stripControlCodes(text), x, y - outline, outlineColor);
        draw(matrix, stripControlCodes(text), x + outline, y, outlineColor);
        draw(matrix, stripControlCodes(text), x - outline, y, outlineColor);
        draw(matrix, text, x, y, color);
    }

    public void drawOutline(MatrixStack matrix, String text, float x, float y, int color) {
        drawOutline(matrix, text, x, y, color, 0.25F);

    }

    public void drawRightOutline(MatrixStack matrix, String text, float x, float y, int color) {
        drawOutline(matrix, text, x - (getWidth(text)), y, color);
    }

    public void drawRightOutline(MatrixStack matrix, String text, double x, double y, int color) {
        drawOutline(matrix, text, (float) (x - (getWidth(text))), (float) y, color);
    }


    public void drawCenterOutline(MatrixStack matrix, String text, float x, float y, int color) {
        drawOutline(matrix, text, x - getWidth(text) / 2f, y, color);
    }

    public void drawCenterOutline(MatrixStack matrix, String text, double x, double y, int color) {
        drawOutline(matrix, text, (float) x - getWidth(text) / 2f, (float) y, color);
    }

    public void draw(MatrixStack matrix, String text, float x, float y, int color, boolean dropShadow) {
        if (dropShadow) {
            this.renderString(matrix, text, x + 0.5F, y + 0.5F, color, true);
        }
        this.renderString(matrix, text, x, y, color, false);
    }

    private void renderString(MatrixStack matrix, String text, float x, float y, int color, boolean dropShadow) {
        if (text != null) {
            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = ColorUtils.multDark(color, 0.2F);
            }
            this.posX = ((x - 1) * 2);
            this.posY = (y * 2);
            this.renderStringAtPos(matrix, text, dropShadow, color);
        }
    }

    private void renderStringAtPos(MatrixStack matrixStack, String text, boolean hasShadow, int color) {
        GlyphPage currentGlyphPage = getCurrentGlyphPage();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL13C.glActiveTexture(GL13C.GL_TEXTURE0);

        //noinspection resource
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        currentGlyphPage.bindTexture();
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < text.length(); ++i) {
            char character = text.charAt(i);
            if (character == 167 && i + 1 < text.length()) {
                int colorIndex = "0123456789abcdefr".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (colorIndex < 16) {
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (hasShadow) {
                        colorIndex += 16;
                    }
                    int colorCode = this.colorCode[colorIndex];
                    red = (float) (colorCode >> 16 & 255) / 255.0F;
                    green = (float) (colorCode >> 8 & 255) / 255.0F;
                    blue = (float) (colorCode & 255) / 255.0F;
                } else {
                    red = (float) (color >> 16 & 255) / 255.0F;
                    green = (float) (color >> 8 & 255) / 255.0F;
                    blue = (float) (color & 255) / 255.0F;
                }
                ++i;
            } else {
                currentGlyphPage = getCurrentGlyphPage();
                currentGlyphPage.bindTexture();
                double charWidth = currentGlyphPage.drawChar(matrixStack, character, posX, posY, red, blue, green, alpha);
                RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                this.posX += (float) charWidth;
            }
        }
        currentGlyphPage.unbindTexture();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private GlyphPage getCurrentGlyphPage() {
        return regularGlyphPage;
    }

    public float getHeight() {
        return regularGlyphPage.getMaxFontHeight() / 2F;
    }


    public float getWidth(String str) {
        String text = stripControlCodes(str);
        float width = 0;
        GlyphPage currentPage;
        int size = text.length();
        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            currentPage = getCurrentGlyphPage();
            width += currentPage.getWidth(character) - 8;
        }
        return width / 2F;
    }


    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String str, int maxWidth, boolean reverse) {
        StringBuilder stringBuilder = new StringBuilder();
        String text = stripControlCodes(str);
        int startIndex = reverse ? text.length() - 1 : 0;
        int increment = reverse ? -1 : 1;
        int currentWidth = 0;
        GlyphPage currentPage;
        for (int i = startIndex; i >= 0 && i < text.length() && i < maxWidth; i += increment) {
            char character = text.charAt(i);
            currentPage = getCurrentGlyphPage();
            currentWidth += (int) ((currentPage.getWidth(character) - 8) / 2);
            if (i > currentWidth) {
                break;
            }
            if (reverse) {
                stringBuilder.insert(0, character);
            } else {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }

    public static String stripControlCodes(String text) {
        char[] chars = text.toCharArray();
        StringBuilder f = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§') {
                i++;
                continue;
            }
            f.append(c);
        }
        return f.toString();
    }

}