package club.lyric.infinity.api.nanovg;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.function.Consumer;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class NanoVGInitializer implements IMinecraft {

    private static long context = 0;

    public static void init()
    {
        context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

        if (context == NULL)
        {
            Infinity.LOGGER.error("Unable to initialize (NanoVGInitializer)", new RuntimeException());
        }
    }

    public static void render(Consumer<Long> drawCall)
    {
        float contentScale = (float) mc.getWindow().getScaleFactor();
        float width  = (int)(mc.getWindow().getFramebufferWidth() / contentScale);
        float height = (int)(mc.getWindow().getFramebufferHeight() / contentScale);

        if (mc.options.hudHidden || mc.world == null) return;

        nvgBeginFrame(context, width, height, contentScale);
        drawCall.accept(context);
        nvgEndFrame(context);
        restoreState();
    }


    private static void restoreState()
    {
        GlStateManager._disableCull();
        GlStateManager._disableDepthTest();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
    }

    public static long getContext()
    {
        return context;
    }
}