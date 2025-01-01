package club.lyric.infinity.api.gui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.util.profiler.Profilers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

/**
 * @author IMGUI devs
 */

public class IMLoader implements IMinecraft {
    private static final Set<RenderableElement> renderstack = new HashSet<>();
    private static final Set<RenderableElement> toRemove = new HashSet<>();
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static ImFont customFont;
    private static ImFont customFontSemiBold;
    private static ImFont customFontSmaller;

    public static void onGlfwInit(long handle) {
        initializeImGui();
        imGuiGlfw.init(handle, true);
        imGuiGl3.init();
    }

    public static void onFrameRender() {
        if (mc.world == null || mc.player == null) return;

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        for (RenderableElement renderable : renderstack) {
            Profilers.get().push("ImGui Render " + renderable.get());
            renderable.getTheme().pre();
            renderable.render();
            renderable.getTheme().post();
            Profilers.get().pop();
        }

        ImGui.render();
        endFrame();
    }

    private static void initializeImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);                               // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        //io.setConfigViewportsNoTaskBarIcon(true);

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide

        final short iconRangeMin = (short) 0xe005;
        final short iconRangeMax = (short) 0xf8ff;
        final short[] iconRange = new short[]{iconRangeMin, iconRangeMax, 0};

        rangesBuilder.addRanges(iconRange);

        final short[] glyphRanges = rangesBuilder.buildRanges();

        ImFontConfig iconsConfig = new ImFontConfig();

        iconsConfig.setMergeMode(true);
        iconsConfig.setPixelSnapH(true);
        iconsConfig.setOversampleH(3);
        iconsConfig.setOversampleV(3);

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();

        fontAtlas.addFontDefault();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/Roboto-Bold.ttf")) {
            if (is != null) {
                byte[] fontData = is.readAllBytes();

                customFontSemiBold = fontAtlas.addFontFromMemoryTTF(fontData, 18);
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }

        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/Roboto-Regular.ttf")) {
            if (is != null) {
                byte[] fontData = is.readAllBytes();

                customFont = fontAtlas.addFontFromMemoryTTF(fontData, 18);
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }

        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/Roboto-Regular.ttf")) {
            if (is != null) {
                byte[] fontData = is.readAllBytes();

                customFontSmaller = fontAtlas.addFontFromMemoryTTF(fontData, 14);
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }


        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        fontConfig.destroy();
        fontAtlas.build();


        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }
    }

    private static void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        if (!toRemove.isEmpty()) {
            toRemove.forEach(renderstack::remove);
            toRemove.clear();
        }
    }

    public static void addRenderable(RenderableElement renderable) {
        renderstack.add(renderable);
    }

    public static void queueRemove(RenderableElement renderable) {
        toRemove.add(renderable);
    }

    public static boolean isRendered(RenderableElement renderable) {
        return renderstack.contains(renderable);
    }

    public static ImFont getCustomFont() {
        return customFont;
    }

    public static ImFont getCustomFontSemiBold() {
        return customFontSemiBold;
    }

    public static ImFont getCustomFontSmaller() {
        return customFontSmaller;
    }

    private IMLoader() {
    }
}