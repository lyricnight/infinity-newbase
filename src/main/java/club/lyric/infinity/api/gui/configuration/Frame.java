package club.lyric.infinity.api.gui.configuration;

import club.lyric.infinity.api.gui.GuiScreen;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Frame implements IMinecraft {

    public DrawContext context;
    public Category moduleCategory;

    // Placement
    private float x;
    private float y;

    // Size
    private float width;
    private float height;

    // Dragging
    public boolean dragging;

    public Frame(Category category, float x, float y, float width, float height) {

        this.moduleCategory = category;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    public Frame(Category category, float x, float y) {
        this(category, x, y, 100f, 13f);
    }

    @SuppressWarnings("DataFlowIssue")
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float delta) {
        this.context = context;

        Color color = Managers.MODULES.getModuleFromClass(Colours.class).getColor();
        Render2DUtils.drawRect(context.getMatrices(), x, y, width, height, new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB());

        Managers.TEXT.drawString(moduleCategory.name(), x + 2.0f, y + 3.0f, new Color(255, 255, 255).getRGB(), true);
    }
}
