package club.lyric.infinity.api.util.client.gui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;

/**
 * category tabs.
 */
public class Tab implements RenderableElement, IMinecraft {
    private boolean first = true;

    private static Tab INSTANCE;

    public Category selected = Category.Client;

    public ImVec2 pos;

    public static Tab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Tab();
        }
        return INSTANCE;
    }

    public ImVec2 getPos() {
        return pos;
    }

    @Override
    public String get() {
        return "Categories";
    }

    @Override
    public void render() {
        if (mc.world == null || mc.player == null) return;
        int imGuiWindowFlags = 0;
        imGuiWindowFlags |= ImGuiWindowFlags.AlwaysAutoResize;
        imGuiWindowFlags |= ImGuiWindowFlags.NoDocking;
        imGuiWindowFlags |= ImGuiWindowFlags.NoDecoration;
        imGuiWindowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus;
        imGuiWindowFlags |= ImGuiWindowFlags.NoFocusOnAppearing;
        ImGui.getStyle().setFramePadding(4, 6);
        ImGui.getStyle().setWindowPadding(16,16);
        ImGui.getStyle().setFrameRounding(8);
        ImGui.getStyle().setWindowRounding(20);
        ImGui.setNextWindowSize(600f, 600f, 0);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.08f, 0.08f, 0.12f, 0.8f);
        ImGui.begin(get(), imGuiWindowFlags);
        ImGui.popStyleColor();

        float posX = (float) (mc.getWindow().getWidth() / 2 - 490);
        float posY = (float) (mc.getWindow().getHeight() / 2 - 250);

        if (first) {
            ImGui.setWindowPos(posX, posY);
            first = false;
        }
        pos = ImGui.getWindowPos();

        float[] color = JColor.getGuiColor().getFloatColor();

        ImGui.indent(27f);
        ImGui.pushFont(IMLoader.getBiggerDosisFont());
        ImGui.pushStyleColor(ImGuiCol.Text, 1f, 1f, 1f, 1.00f);
        ImGui.text("INFINITY");
        ImGui.popFont();
        ImGui.popStyleColor();
        ImGui.unindent(27f);
        ImGui.getStyle().setDisplayWindowPadding(0, 5);

        for (Category category : Category.values()) {
            ImGui.pushID(category.toString());

            if (selected == category) {
                float[] dColor = JColor.getGuiColor().jDarker().getFloatColor();

                ImGui.pushStyleColor(ImGuiCol.Text, 1f, 1f, 1f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, dColor[0], dColor[1], dColor[2], 0.5f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, color[0], color[1], color[2], 0.65f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, color[0], color[1], color[2], 0.7f);
            } else {
                ImGui.pushStyleColor(ImGuiCol.Text, 1f, 1f, 1f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, 0.09f, 0.09f, 0.15f, 0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.09f, 0.09f, 0.15f, 0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.1f, 0.16f, 0f);
            }

            ImGui.pushFont(IMLoader.getBigCustomFont());
            ImGui.button(category.toString(), 165f, 33f);
            ImGui.popFont();
            ImGui.popStyleColor(4);

            if (ImGui.isItemHovered()) {
                if (ImGui.isMouseClicked(1) || ImGui.isMouseClicked(0)) {
                    selected = category;
                    InfinityGUI.getInstance().locY = 0;
                }
            }
            ImGui.popID();
        }
        pos = ImGui.getWindowPos();
        ImGui.end();
    }

    @Override
    public Theme getTheme() {
        return new Theme() {
            @Override
            public void pre() {
                if (mc.world == null || mc.player == null) return;

                float[][] colors = ImGui.getStyle().getColors();

                float[] color = JColor.getGuiColor().getFloatColor();
                float[] bColor = JColor.getGuiColor().jBrighter().getFloatColor();
                float[] dColor = JColor.getGuiColor().jDarker().getFloatColor();

                colors[ImGuiCol.Text]                   = new float[]{0.80f, 0.84f, 0.96f, 1.00f};
                colors[ImGuiCol.TextDisabled]           = new float[]{0.42f, 0.44f, 0.53f, 1.00f};
                colors[ImGuiCol.WindowBg]               = new float[]{0.07f, 0.07f, 0.11f, 1.00f};
                colors[ImGuiCol.ChildBg]                = new float[]{0.09f, 0.09f, 0.15f, 0.00f};
                colors[ImGuiCol.PopupBg]                = new float[]{0.09f, 0.09f, 0.15f, 0.94f};
                colors[ImGuiCol.Border]                 = new float[]{0.42f, 0.44f, 0.53f, 0.50f};
                colors[ImGuiCol.BorderShadow]           = new float[]{0.07f, 0.07f, 0.11f, 0.00f};
                colors[ImGuiCol.FrameBg]                = new float[]{color[0], color[1], color[2], 0.54f};
                colors[ImGuiCol.FrameBgHovered]         = new float[]{color[0], color[1], color[2], 0.40f};
                colors[ImGuiCol.FrameBgActive]          = new float[]{color[0], color[1], color[2], 0.67f};
                colors[ImGuiCol.TitleBg]                = new float[]{0.09f, 0.09f, 0.15f, 1.00f};
                colors[ImGuiCol.TitleBgActive]          = new float[]{0.12f, 0.12f, 0.18f, 1.00f};
                colors[ImGuiCol.TitleBgCollapsed]       = new float[]{0.09f, 0.09f, 0.15f, 0.75f};
                colors[ImGuiCol.MenuBarBg]              = new float[]{0.16f, 0.17f, 0.24f, 1.00f};
                colors[ImGuiCol.ScrollbarBg]            = new float[]{0.14f, 0.15f, 0.20f, 0.53f};
                colors[ImGuiCol.ScrollbarGrab]          = new float[]{0.25f, 0.27f, 0.35f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabHovered]   = new float[]{0.32f, 0.34f, 0.43f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabActive]    = new float[]{0.38f, 0.41f, 0.50f, 1.00f};
                colors[ImGuiCol.CheckMark]              = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.SliderGrab]             = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.SliderGrabActive]       = new float[]{color[0], color[1], color[2], 0.95f};
                colors[ImGuiCol.Button]                 = new float[]{color[0], color[1], color[2], 0.59f};
                colors[ImGuiCol.ButtonHovered]          = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.ButtonActive]           = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Header]                 = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.HeaderHovered]          = new float[]{color[0], color[1], color[2], 0.95f};
                colors[ImGuiCol.HeaderActive]           = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.Separator]              = new float[]{0.45f, 0.47f, 0.58f, 0.50f};
                colors[ImGuiCol.SeparatorHovered]       = new float[]{0.76f, 0.17f, 0.30f, 0.78f};
                colors[ImGuiCol.SeparatorActive]        = new float[]{0.76f, 0.17f, 0.30f, 1.00f};
                colors[ImGuiCol.ResizeGrip]             = new float[]{color[0], color[1], color[2], 0.59f};
                colors[ImGuiCol.ResizeGripHovered]      = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.ResizeGripActive]       = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Tab]                    = new float[]{dColor[0], dColor[1], dColor[2], 0.86f};
                colors[ImGuiCol.TabHovered]             = new float[]{color[0], color[1], color[2], 0.80f};
                colors[ImGuiCol.TabActive]              = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.TabUnfocused]           = new float[]{0.19f, 0.20f, 0.27f, 1.00f};
                colors[ImGuiCol.TabUnfocusedActive]     = new float[]{0.51f, 0.12f, 0.20f, 1.00f};
                colors[ImGuiCol.DockingPreview]         = new float[]{0.26f, 0.59f, 0.98f, 0.70f};
                colors[ImGuiCol.DockingEmptyBg]         = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
                colors[ImGuiCol.PlotLines]              = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
                colors[ImGuiCol.PlotLinesHovered]       = new float[]{1.00f, 0.43f, 0.35f, 1.00f};
                colors[ImGuiCol.PlotHistogram]          = new float[]{0.90f, 0.70f, 0.00f, 1.00f};
                colors[ImGuiCol.PlotHistogramHovered]   = new float[]{1.00f, 0.60f, 0.00f, 1.00f};
                colors[ImGuiCol.TableHeaderBg]          = new float[]{0.19f, 0.19f, 0.20f, 1.00f};
                colors[ImGuiCol.TableBorderStrong]      = new float[]{0.31f, 0.31f, 0.35f, 1.00f};
                colors[ImGuiCol.TableBorderLight]       = new float[]{0.23f, 0.23f, 0.25f, 1.00f};
                colors[ImGuiCol.TableRowBg]             = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.TableRowBgAlt]          = new float[]{1.00f, 1.00f, 1.00f, 0.06f};
                colors[ImGuiCol.TextSelectedBg]         = new float[]{0.90f, 0.27f, 0.33f, 0.35f};
                colors[ImGuiCol.DragDropTarget]         = new float[]{1.00f, 1.00f, 0.00f, 0.90f};
                colors[ImGuiCol.NavHighlight]           = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.NavWindowingHighlight]  = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
                colors[ImGuiCol.NavWindowingDimBg]      = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
                colors[ImGuiCol.ModalWindowDimBg]       = new float[]{0.80f, 0.80f, 0.80f, 0.35f};
                ImGui.getStyle().setColors(colors);

                ImGui.getStyle().setWindowPadding(16,16);
                ImGui.getStyle().setScrollbarSize(10);
                ImGui.getStyle().setWindowTitleAlign(0.5f, 0.5f);

                //if (ImguiLoader.getCustomFont() != null) {
                //    ImGui.pushFont(ImguiLoader.getCustomFont());
                //}
                if (IMLoader.getDosisFont() != null) {
                    ImGui.pushFont(IMLoader.getNormalDosisFont());
                }
            }

            @Override
            public void post() {
                if (mc.world == null || mc.player == null) return;

                if (IMLoader.getCustomFont() != null) {
                    ImGui.popFont();
                }
            }
        };
    }

}