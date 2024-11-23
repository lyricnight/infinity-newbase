package club.lyric.infinity.api.gui;

import club.lyric.infinity.api.gui.components.ModuleComponent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.keyboard.KeyUtils;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class Panel implements IMinecraft {
    private int x;
    private int y;
    private int x2;
    private int y2;
    private final int width;
    private final int height;
    private boolean open;
    public boolean drag;
    private final Category category;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    private final Animation alpha = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    private final Animation opening = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    float currY;
    boolean search = false;
    boolean capsLock = false;
    public ModuleBase moduleBase;

    public String searchText = "c";

    private final ArrayList<ModuleComponent> moduleComponents = new ArrayList<>();


    public Panel(Category category, int x, int y, boolean open) {

        this.category = category;
        this.x = x;
        this.y = y;

        width = 99 + Managers.MODULES.getModuleFromClass(ClickGUI.class).frameWidth.getIValue();
        height = Managers.MODULES.getModuleFromClass(ClickGUI.class).frameHeight.getIValue();
        this.open = open;

        for (ModuleBase modules : Managers.MODULES.getModulesInCategory(category)) {
            this.moduleBase = modules;

            if (!modules.getCategory().equals(category)) continue;

            ModuleComponent moduleComponent = new ModuleComponent(modules, this);

            moduleComponents.add(moduleComponent);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drag(mouseX, mouseY);

        alpha.run(200);
        Color color = ColorUtils.alpha(Managers.MODULES.getModuleFromClass(ClickGUI.class).color.getColor(), (int) alpha.getValue());

        Render2DUtils.drawRect(context.getMatrices(), x, y - 1, width, height, color.getRGB());


        if (open) {

            Render2DUtils.drawRect(context.getMatrices(), x + 0.1f, y + height - 1.0F, width - 0.1f, getHeightTotal() - height + 0.5f, new Color(5, 5, 5, 119).getRGB());

            if (Managers.MODULES.getModuleFromClass(ClickGUI.class).line.value())
                Render2DUtils.drawOutlineRect(context.getMatrices(), x + 0.1f, y + height - 1.0F, width - 0.1f, getHeightTotal() - height + 0.5f, 5.0f, color.getRGB());
        }

        if (isHovering(mouseX, mouseY)) {
            animation.run(2);
        } else {
            animation.run(0);
        }

        currY = height;

        context.getMatrices().push();
        Managers.TEXT.drawString(category.name(), position(), y + (float) height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), -1);
        context.getMatrices().pop();

        if (!open) return;

        //if (search && !moduleBase.getName().contains(searchText)) return;

        for (ModuleComponent component : moduleComponents) {
            component.setY(y + currY);
            currY += component.getHeight() + 1.0f;

            component.render(context, mouseX, mouseY, delta);
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (!drag) {
            return;
        }
        x = x2 + mouseX;
        y = y2 + mouseY;
    }

    public int position()
    {
        ModeSetting module = Managers.MODULES.getModuleFromClass(ClickGUI.class).position;

        if (module.is("Left"))
        {
            return x + 2;
        }
        else if (module.is("Middle"))
        {
           return (int) (x + (float) width / 2 - ((int) Managers.TEXT.width(category.name(), true) >> 1));
        }
        return x + (int) (width - Managers.TEXT.width(category.name(), true));
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY)) {
            x2 = (int) (x - mouseX);
            y2 = (int) (y - mouseY);

            Gui.getPanels().forEach(panel -> panel.drag = false);
            drag = true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isHovering(mouseX, mouseY)) {
            open = !open;
            return;
        }

        if (!open) {
            return;
        }

        for (ModuleComponent component : moduleComponents) {
            component.mouseClicked((int) mouseX, (int) mouseY, button);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            drag = false;
        }

        if (!open) {
            return;
        }

        for (ModuleComponent component : moduleComponents) {
            component.mouseReleased((int) mouseX, (int) mouseY, button);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            alpha.reset();
            opening.reset();
            animation.reset();
        }

        if (keyCode == GLFW.GLFW_KEY_F) {
            search = !search;
        }

        if (keyCode == GLFW.GLFW_KEY_CAPS_LOCK) {
            capsLock = !capsLock;
        }

        if (search) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE)
                setString(backspace(getString()));

            if (capsLock)
                setString(getString() + KeyUtils.getKeyName(keyCode));
            else
                setString(getString() + KeyUtils.getKeyName(keyCode).toLowerCase());

        }


        for (ModuleComponent component : moduleComponents) {
            component.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    public float getHeightTotal() {
        return currY;
    }

    protected boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public String getString() {
        return searchText;
    }

    public void setString(String string) {
        searchText = string;
    }

    public static String backspace(String string) {
        return (string != null && !string.isEmpty()) ? string.substring(0, string.length() - 1) : string;
    }
}