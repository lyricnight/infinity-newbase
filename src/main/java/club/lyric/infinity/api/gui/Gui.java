package club.lyric.infinity.api.gui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class Gui extends Screen implements IMinecraft {
    private static ArrayList<Panel> panels = null;

    public Gui() {
        super(Text.of("Infinity"));

        panels = new ArrayList<>();

        int x = 2;

        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 3, true));

            x += 101 + Managers.MODULES.getModuleFromClass(ClickGUI.class).frameWidth.getIValue();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color color = Managers.MODULES.getModuleFromClass(ClickGUI.class).tintColor.getColor();

        if (Managers.MODULES.getModuleFromClass(ClickGUI.class).tint.value())
            Render2DUtils.drawRect(context.getMatrices(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(color.getRed(), color.getGreen(), color.getBlue(), 50).getRGB());

        panels.forEach(panel -> panel.render(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Managers.MODULES.getModuleFromClass(ClickGUI.class).setEnabled(false);
            close();
            return true;
        }

        panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static ArrayList<Panel> getPanels() {
        return panels;
    }
}