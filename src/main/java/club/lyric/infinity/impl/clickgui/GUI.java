package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.gui.item.Item;
import club.lyric.infinity.api.util.client.gui.item.ModuleButton;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUI extends Screen implements IMinecraft {
    private static Screen parent;
    private final List<Panel> panels = new ArrayList<>();
    private long startTime = 0L;

    public GUI(Screen parent, Text title) {
        super(title);
        Infinity.LOGGER.info("GUI constructed.");
        if (getPanels().isEmpty()) {
            load();
        }
    }

    @Override
    public void init() {
        Infinity.LOGGER.info("GUI is initialising.");
        startTime = System.currentTimeMillis();
        if (!getPanels().isEmpty()) {
            for (Panel panel : getPanels()) {
                Infinity.LOGGER.info("Panel iterated to: " + panel);
                panel.init();
            }
        }
        super.init();
    }

    private void load() {
        Infinity.LOGGER.info("GUI is loading a panel.");
        int x = 10;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x += 110, 20, true) {
                @Override
                public void setupItems() {
                    Managers.MODULES.getModulesInCategory(category).forEach(module -> addButton(new ModuleButton(module)));
                }
                @Override
                public boolean isVisible() {
                    return true;
                }
            });
        }
        panels.forEach(panel -> panel.getItems().sort(Comparator.comparing(Item::getLabel)));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        GL20.glPushMatrix();
        GL20.glEnable(GL20.GL_SCISSOR_TEST);
        RenderSystem.viewport(0, 0, context.getScaledWindowWidth() * 2, context.getScaledWindowHeight() * 2);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        float progress = (float) elapsedTime / 2000;

        float scale = Math.min(1.0f, 0.5f + 0.5f * progress);

        int scissorX = (int) ((context.getScaledWindowWidth() - scale * context.getScaledWindowWidth()) / 2.0f);

        scissorX = Math.min(scissorX, context.getScaledWindowWidth());

        GL20.glScissor(scissorX * 2, 0, (context.getScaledWindowWidth() - scissorX) * 2, context.getScaledWindowHeight() * 2);

        GL20.glScalef(scale, scale, 1.0f);

        GL20.glPushAttrib(GL20.GL_SCISSOR_BIT);

        panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, delta));

        GL20.glPopAttrib();
        GL20.glDisable(GL20.GL_SCISSOR_TEST);
        GL20.glPopMatrix();
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public List<Panel> getPanels() {
        return this.panels;
    }

    public static Screen getParent()
    {
        return parent;
    }

    @Override
    public void close()
    {
        Infinity.LOGGER.info("GUI close signal given.");
        mc.setScreen(null);
        super.close();
    }
}