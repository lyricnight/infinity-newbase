package club.lyric.infinity.api.gui.configuration;

import club.lyric.infinity.api.gui.configuration.components.ModuleComponent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author valser
 */
public class Frame implements IMinecraft {

    public DrawContext context;
    public Category moduleCategory;

    // Placement
    public float x;
    public float y;

    // Size
    private final float width;
    private final float height;

    // Dragging
    private boolean dragging;
    ArrayList<ModuleComponent> components = new ArrayList<>();
    private float off;


    public Frame(Category category, float x, float y, float width, float height) {

        this.moduleCategory = category;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        /*for (ModuleBase module : Managers.MODULES.getModules())
        {
            if (module.getCategory() == category) {
                components.add(new ModuleComponent(module, this, x, y));
            }
        }*/
    }

    public Frame(Category category, float x, float y) {
        this(category, x, y, 100f, 13f);
    }

    @SuppressWarnings("DataFlowIssue")
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float delta) {
        this.context = context;

        if (dragging) {
            this.x = mouseX;
            this.y = mouseY;
        }

        Color color = Managers.MODULES.getModuleFromClass(Colours.class).getColor();
        Render2DUtils.drawRect(context.getMatrices(), x, y, width, height, new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB());

        Managers.TEXT.drawString(moduleCategory.name(), x + 2.0f, y + 3.0f, new Color(255, 255, 255).getRGB(), true);

        off = y + height + 1.0f;

        /**for (ModuleComponent moduleButton : components) {
            moduleButton.drawScreen(context, (int) x + 1, (int) off + 1, delta);
            off += 13.5f;
        }*/
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY)) {
            dragging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY)) {
            dragging = false;
        }
    }

    protected boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void close() {
        dragging = false;
    }
}
