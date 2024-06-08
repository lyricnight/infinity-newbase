package club.lyric.infinity.api.util.client.gui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.manager.Managers;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;

public class Tabs implements RenderableElement {

    public Category category;

    private boolean first;

    private boolean isFocused;

    private float posX;

    private float posY;

    public float scrollPos;

    public Tabs(Category category, float posX, float posY)
    {
        this.category = category;
        this.posX = posX;
        this.posY = posY;
        this.scrollPos = 0;
        this.first = true;
        this.isFocused = false;
    }

    public boolean isFocused()
    {
        return isFocused;
    }

    @Override
    public String get()
    {
        return category.name();
    }

    @Override
    public void render() {
        int imGuiWindowFlags = 0;
        imGuiWindowFlags |= ImGuiWindowFlags.AlwaysAutoResize;
        imGuiWindowFlags |= ImGuiWindowFlags.NoDocking;
        ImGui.getStyle().setFramePadding(4, 6);
        ImGui.getStyle().setButtonTextAlign(0, 0.5f);
        ImGui.begin(get(), imGuiWindowFlags);

        isFocused = ImGui.isWindowHovered() || ImGui.isWindowFocused();

        if (scrollPos > ImGui.getScrollMaxY()) scrollPos = ImGui.getScrollMaxY();
        else if (scrollPos < 0) scrollPos = 0;
        ImGui.setScrollY(scrollPos);

        if (first) {
            ImGui.setWindowPos(posX, posY);
            first = false;
        }

        for (ModuleBase module : Managers.MODULES.getModulesInCategory(category)) {
            ImGui.pushID(module.getName());

            if (module.isOn()) {
                float[] color = JColor.getGuiColor().getFloatColor();

                ImGui.pushStyleColor(ImGuiCol.Text, 0.80f, 0.84f, 0.96f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, color[0], color[1], color[2], 0.50f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, color[0], color[1], color[2], 0.65f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, color[0], color[1], color[2], 0.8f);
            } else {
                ImGui.pushStyleColor(ImGuiCol.Text, 0.42f, 0.44f, 0.53f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, 0.07f, 0.07f, 0.11f, 0.f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.09f, 0.09f, 0.15f, 0.65f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.1f, 0.16f, 0.8f);
            }

            boolean isToggled = ImGui.button(module.getName(), 180f, 30f);

            ImGui.popStyleColor(4);

            if (isToggled) {
                module.toggle();
            }

            if (ImGui.isItemHovered()) {
                ImGui.setTooltip(module.getDescription());

                if (ImGui.isMouseClicked(1)) {
                    module.toggleShowSettings();
                }
            }

            if (module.showSettings()) {
                ImGui.indent(10f);
                ImGui.pushFont(IMLoader.getDosisFont());
                ImGui.getStyle().setFramePadding(4, 4);
                ImGui.getStyle().setButtonTextAlign(0.5f, 0.5f);
                module.renderSettings();
                ImGui.getStyle().setButtonTextAlign(0f, 0f);
                ImGui.getStyle().setFramePadding(4, 6);
                ImGui.popFont();
                ImGui.unindent(10f);
            }

            ImGui.popID();
        }

        ImGui.end();
    }

    @Override
    public Theme getTheme() {
        return Menu.getInstance().getTheme();
    }
}
