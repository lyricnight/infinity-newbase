package club.lyric.infinity.api.gui.configuration;

import club.lyric.infinity.api.gui.GuiScreen;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Frame {

    public DrawContext context;
    public Category moduleCategory;

    // Placement
    private static float x;
    private static float y;

    // Size
    private static float width;
    private static float height;

    // Dragging
    public static boolean dragging;

    public Frame(Category category, float x, float y, float width, float height) {

        this.moduleCategory = category;

        Frame.x = x;
        Frame.y = y;

        Frame.width = width;
        Frame.height = height;

    }

    public Frame(Category category, float x, float y) {
        this(category, x, y, 100f, 13f);
    }

    @SuppressWarnings("DataFlowIssue")
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.context = context;

        drag(mouseX, mouseY);
        if (isHovering(mouseX, mouseY)) {
            dragging = true;
        }

        Render2DUtils.drawRect(context.getMatrices(), x, y, width, height, Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB());

        Managers.TEXT.drawString(moduleCategory.name(), x + 3.0f, y + 3.0f, new Color(255, 255, 255).getRGB(), true);
    }

    private void drag(int mouseX, int mouseY) {
        if (!dragging) {
            return;
        }
        x = GuiScreen.lastMouseX + mouseX;
        y = GuiScreen.lastMouseY + mouseY;
    }


    public static boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
