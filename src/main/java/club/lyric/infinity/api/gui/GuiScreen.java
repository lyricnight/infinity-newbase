package club.lyric.infinity.api.gui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.gui.configuration.Frame;
import club.lyric.infinity.api.gui.interfaces.Screening;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author valser
 */
public class GuiScreen extends Screening {

    // Detects whether you are clicking.
    public static boolean leftClick;
    public static boolean rightClick;

    // Detects whether you are holding. (Used for dragging.)
    public static boolean leftHold;
    public static boolean rightHold;

    public static final List<Frame> frames = new CopyOnWriteArrayList<>();

    public GuiScreen() {
        float x = 2.0f;
        for (Category category : Category.values()) {
            frames.add(new Frame(category, x, 2.0f));
            x += 102.0f;
        }
    }

    /**
     * @param context = net/minecraft/client/gui/DrawContext
     * @param mouseX = x coordinates of the mouse
     * @param mouseY = y coordinates of the mouse
     * @param delta = delta
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // renders black background
        renderBackground(context, mouseX, mouseY, delta);

        frames.forEach(frame -> frame.drawScreen(context, mouseX, mouseY, delta));
    }

    /**
     * @param mouseX = x coordinates of the mouse
     * @param mouseY = y coordinates of the mouse
     * @param mouseButton = 0: left button, 1: right button
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            leftClick = true;
            leftHold = true;
        }
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            Infinity.LOGGER.info("right click");
            rightClick = true;
            rightHold = true;
        }
        frames.forEach(frame -> frame.mouseClicked((int) mouseX, (int) mouseY, mouseButton));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * @param mouseX = x coordinates of the mouse
     * @param mouseY = y coordinates of the mouse
     * @param mouseButton = 0: left button, 1: right button
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            leftClick = false;
            leftHold = false;
        }
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            rightClick = false;
            rightHold = false;
        }
        frames.forEach(frame -> frame.mouseReleased((int) mouseX, (int) mouseY, mouseButton));
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            mc.setScreen(null);
            Managers.MODULES.getModuleFromClass(GuiRewrite.class).setEnabled(false);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        frames.forEach(Frame::close);
    }

}
