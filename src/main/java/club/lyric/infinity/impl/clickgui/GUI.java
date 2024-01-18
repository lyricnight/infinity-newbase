package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.gui.Panel;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.Item;
import club.lyric.infinity.api.util.client.gui.item.ModuleButton;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class GUI extends Screen implements IMinecraft {
    private static GUI clickGui;
    private final List<Panel> panels = new ArrayList<>();
    private long startTime = 0L;

    protected GUI(Text title) {
        super(title);
        if (getPanels().isEmpty()) {
            load();
        }
    }

    public static GUI getClickGui() {
        return clickGui == null ? (clickGui = new GUI(Text.of("Infinity"))) : clickGui;
    }

    @Override
    public void init() {
        super.init();
        this.startTime = System.currentTimeMillis();
        if (!getPanels().isEmpty()) {
            for (Panel panel : getPanels()) {
                panel.init();
            }
        }
    }

    private void load() {
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

        glPushMatrix();
        glEnable(GL_SCISSOR_TEST);
        glViewport(0, 0, context.getScaledWindowWidth() * 2, context.getScaledWindowHeight() * 2);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        float progress = (float) elapsedTime / 2000;

        float scale = Math.min(1.0f, 0.5f + 0.5f * progress);

        int scissorX = (int) ((context.getScaledWindowWidth() - scale * context.getScaledWindowWidth()) / 2.0f);

        scissorX = Math.min(scissorX, context.getScaledWindowWidth());

        glScissor(scissorX * 2, 0, (context.getScaledWindowWidth() - scissorX) * 2, context.getScaledWindowHeight() * 2);

        glScalef(scale, scale, 1.0f);

        glPushAttrib(GL_SCISSOR_BIT);

        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, delta));

        glPopAttrib();
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();

        //if (progress >= 1.0f) {
        //    renderGradient();
        //}
    }

    /*
    private void renderGradient() {
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();

        Rect bottom = new Rect(0, -85, screenWidth, screenHeight + 85);
        Renderer.renderRectRollingRainbow(bottom, 180, 0);
    }
    */
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        this.drawDefaultBackground();
//        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
//    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public List<Panel> getPanels() {
        return this.panels;
    }
}