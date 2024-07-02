package club.lyric.infinity.api.util.client.render.util.nvg;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Fonts;
import club.lyric.infinity.manager.Managers;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * @author me and lwjgl. (recreated the class from 3arthh4ck fabric)
 */
@SuppressWarnings("ConstantConditions")
public class NVGRenderer implements IMinecraft {

    private int program, blendSrc, blendDst, stencilMask, stencilRef, stencilFuncMask, activeTexture, vertexArray, arrayBuffer, textureBinding;
    private boolean depthTest, scissorTest, init = false;
    private final boolean[] colorMask = new boolean[4];
    private static final float blur = 0.0f;
    private ByteBuffer buf = null;
    private long context = 0;
    private int id = -1;

    public void initialize() {
        context = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS);

        try {
            byte[] fontBytes = Managers.MODULES.getModuleFromClass(Fonts.class).selectedFont();

            destroyBuffer();

            buf = MemoryUtil.memAlloc(fontBytes.length);
            buf.put(fontBytes);
            buf.flip();

            if (NanoVG.nvgCreateFontMem(context, Managers.MODULES.getModuleFromClass(Fonts.class).getFontName(), buf, false) == -1)
                throw new RuntimeException("Failed to create font " + Managers.MODULES.getModuleFromClass(Fonts.class).getFontName());

            id = NanoVG.nvgFindFont(context, Managers.MODULES.getModuleFromClass(Fonts.class).getFontName());

            if (id == -1) {
                Managers.MODULES.getModuleFromClass(Fonts.class).setEnabled(false);
            }

            init = true;
        } catch (Exception e) {

            Infinity.LOGGER.error(e);

            Managers.MODULES.getModuleFromClass(Fonts.class).setEnabled(false);
            ChatUtils.sendMessagePrivate("Failed to load the font " + Managers.MODULES.getModuleFromClass(Fonts.class).getFontName() + ".");
        }
    }

    public void destroyBuffer() {
        if (buf != null) {
            MemoryUtil.memFree(buf);

            buf = null;
        }
    }

    private void textSized(String text, float x, float y, Color color, boolean shadow) {
        NanoVG.nvgBeginPath(context);

        NanoVG.nvgFontFaceId(context, id);
        NanoVG.nvgFontSize(context, Managers.MODULES.getModuleFromClass(Fonts.class).size.getFValue() - 10);
        NanoVG.nvgTextAlign(context, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);

        if (shadow) {
            Color shadowColor = new Color(ColorUtils.darken(color, 200).getRGB());

            NanoVG.nvgFontBlur(context, blur + 0.5f);
            NanoVG.nvgFillColor(context, getColorNVG(shadowColor));
            NanoVG.nvgText(context, x + 0.5f, y + 0.5f, text);
        }

        NanoVG.nvgFillColor(context, getColorNVG(color));
        NanoVG.nvgFontBlur(context, blur);
        NanoVG.nvgText(context, x, y, text);

        NanoVG.nvgClosePath(context);
    }

    public void drawText(String text, float x, float y, Color color, boolean shadow) {
        Color activeColor;
        text = text.trim();

        String[] textParts = text.split("§");

        if (textParts.length == 1) {
            textSized(text, x, y, color, shadow);

            return;
        }

        for (String s : textParts) {
            if (s.isEmpty()) continue;

            switch (s.charAt(0)) {

                case '0' -> activeColor = new Color(0);
                case '1' -> activeColor = new Color(170);
                case '2' -> activeColor = new Color(43520);
                case '3' -> activeColor = new Color(43690);
                case '4' -> activeColor = new Color(11141120);
                case '5' -> activeColor = new Color(11141290);
                case '6' -> activeColor = new Color(16755200);
                case '7' -> activeColor = new Color(11184810);
                case '8' -> activeColor = new Color(5592405);
                case '9' -> activeColor = new Color(5592575);
                case 'a' -> activeColor = new Color(5635925);
                case 'b' -> activeColor = new Color(5636095);
                case 'c' -> activeColor = new Color(16733525);
                case 'd' -> activeColor = new Color(16733695);
                case 'e' -> activeColor = new Color(16777045);
                case 'f' -> activeColor = new Color(16777215);

                default -> activeColor = color;

            }

            if (s.length() > 1) {
                if (activeColor != color) s = s.substring(1);

                textSized(s, x, y, activeColor, true);
                x += getWidth(s) + (s.endsWith(" ") ? getWidth("a") / 1.5f : 0);

            }
        }
    }

    public static NVGColor getColorNVG(Color color) {
        NVGColor clr = NVGColor.create();

        clr.r(color.getRed() / 255.0f);
        clr.g(color.getGreen() / 255.0f);
        clr.b(color.getBlue() / 255.0f);
        clr.a(color.getAlpha() / 255.0f);

        return clr;
    }

    public float getWidth(String text) {
        text = text.replaceAll("§[0-9abcdeflmno]", "");

        float[] bounds = new float[4];

        NanoVG.nvgTextBounds(context, 0, 0, text, bounds);

        return bounds[2] - bounds[0];
    }

    public float getFontHeight() {

        float[] ascent = new float[1];
        float[] descent = new float[1];
        float[] lineHeight = new float[1];

        NanoVG.nvgFontFaceId(context, id);
        NanoVG.nvgFontSize(context, Managers.MODULES.getModuleFromClass(Fonts.class).size.getFValue() - 10);
        NanoVG.nvgTextMetrics(context, ascent, descent, lineHeight);

        return ascent[0] - descent[0];
    }

    public void startDraw() {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        glGetIntegerv(GL_CURRENT_PROGRAM, buffer);
        program = buffer.get(0);

        blendSrc = glGetInteger(GL_BLEND_SRC);
        blendDst = glGetInteger(GL_BLEND_DST);

        depthTest = glIsEnabled(GL_DEPTH_TEST);
        scissorTest = glIsEnabled(GL_SCISSOR_TEST);

        ByteBuffer colorMaskBuffer = BufferUtils.createByteBuffer(4);
        glGetBooleanv(GL_COLOR_WRITEMASK, colorMaskBuffer);
        for (int i = 0; i < 4; i++) colorMask[i] = colorMaskBuffer.get(i) != 0;

        buffer.clear();

        stencilMask = glGetInteger(GL_STENCIL_WRITEMASK);
        stencilRef = glGetInteger(GL_STENCIL_FUNC);
        stencilFuncMask = glGetInteger(GL_STENCIL_VALUE_MASK);

        activeTexture = glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        vertexArray = glGetInteger(GL_VERTEX_ARRAY_BINDING);
        arrayBuffer = glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);

        textureBinding = glGetInteger(GL_TEXTURE_BINDING_2D);

        NanoVG.nvgBeginFrame(context, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 3f);
    }

    public void endDraw() {
        NanoVG.nvgEndFrame(context);

        glUseProgram(program);
        glBlendFunc(blendSrc, blendDst);

        setGlState(GL_DEPTH_TEST, depthTest);
        setGlState(GL_SCISSOR_TEST, scissorTest);

        glColorMask(colorMask[0], colorMask[1], colorMask[2], colorMask[3]);

        glStencilMask(stencilMask);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        glStencilFunc(stencilRef, stencilFuncMask, 0xffffffff);

        GL13.glActiveTexture(activeTexture);

        glBindVertexArray(vertexArray);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBuffer);
        glBindTexture(GL_TEXTURE_2D, textureBinding);
    }

    private void setGlState(int cap, boolean state) {
        if (state) glEnable(cap);
        else glDisable(cap);
    }

    public boolean isInitialized() {
        return init;
    }

    public void reInit() {
        init = false;

        Managers.MODULES.getModuleFromClass(Fonts.class).setEnabled(false);
        Managers.MODULES.getModuleFromClass(Fonts.class).setEnabled(true);
    }

}