package club.lyric.infinity.api.util.client.gui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class IMLoader implements IMinecraft {
    private static final Set<RenderableElement> renderstack = new HashSet<>();
    private static final Set<RenderableElement> toRemove = new HashSet<>();
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static ImFont customFont;
    private static ImFont bigCustomFont;
    private static ImFont biggerCustomFont;
    private static ImFont normalDosisFont;
    private static ImFont dosisFont;
    private static ImFont bigDosisFont;
    private static ImFont biggerDosisFont;
    private static ImFont fontAwesome;
    private static ImFont normalFontAwesome;
    private static ImFont bigFontAwesome;
    private static ImFont biggerFontAwesome;

    public static void onGlfwInit(long handle) {
        initializeImGui();
        imGuiGlfw.init(handle,true);
        imGuiGl3.init();
    }

    public static void onFrameRender() {
        if (mc.world == null || mc.player == null) return;

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        for (RenderableElement renderable : renderstack) {
            mc.getProfiler().push("ImGui Render " + renderable.get());
            renderable.getTheme().pre();
            renderable.render();
            renderable.getTheme().post();
            mc.getProfiler().pop();
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
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontAtlas.addFontDefault();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        byte[] fontAwesomeData = null;
        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/FontAwesome6-Solid.otf")) {
            if (is != null) {
                fontAwesomeData = is.readAllBytes();
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }

        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/museo-sans-rounded-500.ttf")) {
            if (is != null) {
                byte[] fontData = is.readAllBytes();

                customFont = fontAtlas.addFontFromMemoryTTF(fontData, 18);
                bigCustomFont = fontAtlas.addFontFromMemoryTTF(fontData, 21);
                biggerCustomFont = fontAtlas.addFontFromMemoryTTF(fontData, 32);
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }

        byte[] dosisFontData = null;
        try (InputStream is = IMLoader.class.getClassLoader().getResourceAsStream("assets/Museo-Sans-Cyrl-900.ttf")) {
            if (is != null) {
                dosisFontData = is.readAllBytes();

                normalDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 20);
                bigDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 24);
                biggerDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 32);
                dosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 18);
            }
        } catch (IOException ignored) {
            Infinity.LOGGER.atError();
        }
        fontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 20, iconsConfig, iconRange);


        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        dosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 18);
        fontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 18, iconsConfig, iconRange);
        bigDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 24);
        bigFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 24, iconsConfig, iconRange);
        biggerDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 32);
        biggerFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 32, iconsConfig, iconRange);
        normalDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 20);
        normalFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 20, iconsConfig, iconRange);
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

    public static ImFont getBigCustomFont() {
        return bigCustomFont;
    }

    public static ImFont getBigDosisFont() {
        return bigDosisFont;
    }

    public static ImFont getBigFontAwesome() {
        return bigFontAwesome;
    }

    public static ImFont getBiggerCustomFont() {
        return biggerCustomFont;
    }

    public static ImFont getBiggerDosisFont() {
        return biggerDosisFont;
    }

    public static ImFont getBiggerFontAwesome() {
        return biggerFontAwesome;
    }

    public static ImFont getCustomFont() {
        return customFont;
    }

    public static ImFont getDosisFont() {
        return dosisFont;
    }

    public static ImFont getFontAwesome() {
        return fontAwesome;
    }

    public static ImFont getNormalDosisFont() {
        return normalDosisFont;
    }

    public static ImFont getNormalFontAwesome() {
        return normalFontAwesome;
    }
    private IMLoader() {}
}