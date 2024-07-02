package club.lyric.infinity.api.util.client.gui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;

import java.util.ArrayList;
import java.util.List;

public final class Menu implements RenderableElement {
    private static Menu INSTANCE;
    public final List<Tabs> tabs = new ArrayList<>();

    public static Menu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Menu();
        }
        if (INSTANCE.tabs.isEmpty()) {
            float x = 10f;
            for (Category category : Category.values()) {
                INSTANCE.tabs.add(new Tabs(category, x, 10f));
                x += 240f;
            }
        }
        return INSTANCE;
    }

    public void toggle() {
        if (IMLoader.isRendered(getInstance())) {
            IMLoader.queueRemove(getInstance());
        } else {
            IMLoader.addRenderable(getInstance());
        }
    }

    @Override
    public String get() {
        return Infinity.CLIENT_NAME;
    }

    @Override
    public void render() {
        for (Tabs tabsVar : tabs) {
            tabsVar.render();
        }
    }

    @Override
    public Theme getTheme() {
        return new Theme() {
            @Override
            public void pre() {
                float[][] colors = ImGui.getStyle().getColors();

                float[] color = JColor.getGuiColor().getFloatColor();
                float[] bColor = JColor.getGuiColor().jBrighter().getFloatColor();
                float[] dColor = JColor.getGuiColor().jDarker().getFloatColor();

                colors[ImGuiCol.Text] = new float[]{1.00f, 1.00f, 1.00f, 1.00f};
                colors[ImGuiCol.TextDisabled] = new float[]{0.41f, 0.41f, 0.41f, 1.00f};
                colors[ImGuiCol.WindowBg] = new float[]{0.0352941176f, 0.0352941176f, 0.0352941176f, 1.00f};
                colors[ImGuiCol.ChildBg] = new float[]{0.09f, 0.09f, 0.15f, 0.00f};
                colors[ImGuiCol.PopupBg] = new float[]{0.09f, 0.09f, 0.15f, 0.94f};
                colors[ImGuiCol.FrameBg] = new float[]{color[0], color[1], color[2], 0.54f};
                colors[ImGuiCol.FrameBgHovered] = new float[]{color[0], color[1], color[2], 0.40f};
                colors[ImGuiCol.FrameBgActive] = new float[]{color[0], color[1], color[2], 0.67f};
                colors[ImGuiCol.TitleBg] = new float[]{0.0392f, 0.0392f, 0.0392f, 1.00f};
                colors[ImGuiCol.TitleBgActive] = new float[]{0.0392f, 0.0392f, 0.0392f, 1.00f};
                colors[ImGuiCol.TitleBgCollapsed] = new float[]{0.0392f, 0.0392f, 0.0392f, 1.00f};
                colors[ImGuiCol.MenuBarBg] = new float[]{0.16f, 0.17f, 0.24f, 1.00f};
                colors[ImGuiCol.ScrollbarBg] = new float[]{0.14f, 0.15f, 0.20f, 0.53f};
                colors[ImGuiCol.ScrollbarGrab] = new float[]{0.25f, 0.27f, 0.35f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabHovered] = new float[]{0.32f, 0.34f, 0.43f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabActive] = new float[]{0.38f, 0.41f, 0.50f, 1.00f};
                colors[ImGuiCol.CheckMark] = new float[]{1.00f, 1.00f, 1.00f, 1.00f};
                colors[ImGuiCol.SliderGrab] = new float[]{0.0470588235f, 0.0470588235f, 0.0470588235f, 1.00f};
                colors[ImGuiCol.SliderGrabActive] = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Button] = new float[]{0.0470588235f, 0.0470588235f, 0.0470588235f, 0.59f};
                colors[ImGuiCol.ButtonHovered] = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.ButtonActive] = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Header] = new float[]{0.0549019608f, 0.0549019608f, 0.0549019608f, 1.00f};
                colors[ImGuiCol.HeaderHovered] = new float[]{color[0], color[1], color[2], 0.00f};
                colors[ImGuiCol.HeaderActive] = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.Separator] = new float[]{0.45f, 0.47f, 0.58f, 0.50f};
                colors[ImGuiCol.SeparatorHovered] = new float[]{0.76f, 0.17f, 0.30f, 0.78f};
                colors[ImGuiCol.SeparatorActive] = new float[]{0.76f, 0.17f, 0.30f, 1.00f};
                colors[ImGuiCol.ResizeGrip] = new float[]{color[0], color[1], color[2], 0.59f};
                colors[ImGuiCol.ResizeGripHovered] = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.ResizeGripActive] = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Tab] = new float[]{dColor[0], dColor[1], dColor[2], 0.86f};
                colors[ImGuiCol.TabHovered] = new float[]{color[0], color[1], color[2], 0.80f};
                colors[ImGuiCol.TabActive] = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.TabUnfocused] = new float[]{0.19f, 0.20f, 0.27f, 1.00f};
                colors[ImGuiCol.TabUnfocusedActive] = new float[]{0.51f, 0.12f, 0.20f, 1.00f};
                colors[ImGuiCol.DockingPreview] = new float[]{0.26f, 0.59f, 0.98f, 0.70f};
                colors[ImGuiCol.DockingEmptyBg] = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
                colors[ImGuiCol.PlotLines] = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
                colors[ImGuiCol.PlotLinesHovered] = new float[]{1.00f, 0.43f, 0.35f, 1.00f};
                colors[ImGuiCol.PlotHistogram] = new float[]{0.90f, 0.70f, 0.00f, 1.00f};
                colors[ImGuiCol.PlotHistogramHovered] = new float[]{1.00f, 0.60f, 0.00f, 1.00f};
                colors[ImGuiCol.TableHeaderBg] = new float[]{0.19f, 0.19f, 0.20f, 1.00f};
                colors[ImGuiCol.TableBorderStrong] = new float[]{0.31f, 0.31f, 0.35f, 1.00f};
                colors[ImGuiCol.TableBorderLight] = new float[]{0.23f, 0.23f, 0.25f, 1.00f};
                colors[ImGuiCol.TableRowBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.TableRowBgAlt] = new float[]{1.00f, 1.00f, 1.00f, 0.06f};
                colors[ImGuiCol.TextSelectedBg] = new float[]{0.90f, 0.27f, 0.33f, 0.35f};
                colors[ImGuiCol.DragDropTarget] = new float[]{1.00f, 1.00f, 0.00f, 0.90f};
                colors[ImGuiCol.NavHighlight] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.NavWindowingHighlight] = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
                colors[ImGuiCol.NavWindowingDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
                colors[ImGuiCol.ModalWindowDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.35f};
                ImGui.getStyle().setColors(colors);

                ImGui.getStyle().setWindowRounding(4);
                ImGui.getStyle().setFrameRounding(2);
                ImGui.getStyle().setGrabRounding(2);
                ImGui.getStyle().setPopupRounding(2);

                ImGui.getStyle().setScrollbarSize(10);
                ImGui.getStyle().setScrollbarRounding(2);

                ImGui.getStyle().setTabRounding(2);

                ImGui.getStyle().setAntiAliasedFill(true);
                ImGui.getStyle().setAntiAliasedLines(true);
                if (IMLoader.getCustomFont() != null) {
                    ImGui.pushFont(IMLoader.getCustomFontSemiBold());
                }

            }

            @Override
            public void post() {
                if (IMLoader.getCustomFont() != null) {
                    ImGui.popFont();
                }
            }
        };
    }
}
