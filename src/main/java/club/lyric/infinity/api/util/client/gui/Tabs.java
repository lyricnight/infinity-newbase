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
        ImGui.getStyle().setFramePadding(0, 5);
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

                ImGui.pushStyleColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, color[0], color[1], color[2], 1.00f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, color[0], color[1], color[2], 0.65f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, color[0], color[1], color[2], 1.00f);
            } else {
                ImGui.pushStyleColor(ImGuiCol.Text, 0.41f, 0.41f, 0.41f, 1.00f);
                ImGui.pushStyleColor(ImGuiCol.Button, 0.0941f, 0.0941f, 0.0941f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.101f, 0.101f, 0.101f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.0941f, 0.0941f, 0.0941f, 1.0f);
            }

            boolean isToggled = ImGui.button(module.getName(), 220f, 30f);

            ImGui.popStyleColor(4);

            if (isToggled) {
                module.toggle();
            }

            if (ImGui.isItemHovered()) {
                if (ImGui.isMouseClicked(1)) {
                    module.toggleShowSettings();
                }
            }

            if (module.showSettings()) {
                ImGui.indent(4f);
                ImGui.pushFont(IMLoader.getCustomFont());
                ImGui.getStyle().setFramePadding(2, 3);
                ImGui.getStyle().setButtonTextAlign(0.5f, 0.5f);
                module.renderSettings();
                ImGui.getStyle().setButtonTextAlign(0f, 0f);
                ImGui.getStyle().setFramePadding(2, 3);
                ImGui.popFont();
                ImGui.unindent(4f);
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
