package club.lyric.infinity.api.gui;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class GuiScreen extends Screen implements IMinecraft {

    float lastMouseX;
    float lastMouseY;

    // Detects whether you are clicking.
    public static boolean leftClick;
    public static boolean rightClick;

    // Detects whether you are holding. (Used for dragging.)
    public static boolean leftHold;
    public static boolean rightHold;

    public GuiScreen() {
        super(Text.literal("Infinity"));
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

        // mouse pos
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        // Makes it so only our gui changes scale.
        context.getMatrices().push();

        // Scaling gui.
        float scaling = Managers.MODULES.getModuleFromClass(GuiRewrite.class).getScaledNumber();
        context.getMatrices().scale(scaling, scaling, scaling);

        context.getMatrices().pop();

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
            leftClick = false;
            leftHold = false;
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
