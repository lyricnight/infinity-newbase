package club.lyric.infinity.api.gui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.gui.configuration.Frame;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiScreen extends Screen implements IMinecraft {

    public static float lastMouseX;
    public static float lastMouseY;

    // Detects whether you are clicking.
    public static boolean leftClick;
    public static boolean rightClick;

    // Detects whether you are holding. (Used for dragging.)
    public static boolean leftHold;
    public static boolean rightHold;

    public static final List<Frame> frames = new CopyOnWriteArrayList<>();

    public GuiScreen() {
        super(Text.literal("Infinity"));

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
    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // renders black background
        renderBackground(context);

        // Makes it so only our gui changes scale.
        context.getMatrices().push();

        // Scaling gui.
        float scaling = Managers.MODULES.getModuleFromClass(GuiRewrite.class).getScaledNumber();
        context.getMatrices().scale(scaling, scaling, scaling);

        frames.forEach(frame -> frame.drawScreen(context, mouseX, mouseY, delta));

        context.getMatrices().pop();

        // mouse pos
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    /**
     * @param mouseX = x coordinates of the mouse
     * @param mouseY = y coordinates of the mouse
     * @param mouseButton = 0: left button, 1: right button
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (Frame.isHovering((int) mouseX, (int) mouseY) && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            Infinity.LOGGER.info("left lcick");
            Frame.dragging = true;
        }
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            Infinity.LOGGER.info("right click");
            rightClick = true;
            rightHold = true;
        }
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
            Frame.dragging = false;
        }
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            rightClick = false;
            rightHold = false;
        }
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
    public boolean shouldPause() {
        return false;
    }

}
