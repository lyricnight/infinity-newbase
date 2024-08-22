package club.lyric.infinity.api.gui.components.settings;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModeComponent extends Component implements IMinecraft {

    public ModeSetting setting;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());

    // Mode
    private final Animation animationMode = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    private boolean opened;
    private int modeHeight;

    public ModeComponent(ModeSetting setting, Panel panel) {
        this.panel = panel;
        this.setting = setting;
        this.height = Managers.MODULES.getModuleFromClass(ClickGUI.class).buttonHeight.getIValue();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        Color color = ColorUtils.alpha(Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor((int) y) : Managers.MODULES.getModuleFromClass(Colours.class).getColor(), 200);

        rect.run(width - 2.0f);

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 2.0f, y, rect.getValue(), height, color.getRGB());

        if (isHovering(mouseX, mouseY) && Managers.MODULES.getModuleFromClass(ClickGUI.class).hover.value()) {
            animation.run(2);
        } else {
            animation.run(0);
        }

        context.getMatrices().push();
        Managers.TEXT.drawString(setting.getName() + ": " + setting.getMode(), (int) (panel.getX() + 4.0f), y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), -1);
        context.getMatrices().pop();
        animation.reset();
        rect.reset();

        modeHeight = 0;

        if (mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + modeHeight + 11.0f + height / 2 - (Managers.TEXT.height(true) >> 1) + height - 1) {
            animationMode.run(1);
        }
        else
        {
            animationMode.run(0);
        }

        if (opened) {

            for (String string : setting.modes) {

                Color textColor;

                if (!setting.getMode().equals(string))
                {
                    textColor = new Color(170, 170, 170);
                }
                else
                {
                    textColor = new Color(255, 255, 255);
                }

                float modeY = y + modeHeight;

                context.getMatrices().push();
                Managers.TEXT.drawString(string, (int) (panel.getX() + 4.0f), modeY + height / 2 - (Managers.TEXT.height(true) >> 1) - animationMode.getValue() + height, textColor.getRGB());
                context.getMatrices().pop();

                modeHeight += 11;
            }
            Render2DUtils.drawOutlineRect(context.getMatrices(), panel.getX() + 2.1f, y + height, width - 2.1f, modeHeight + 0.5f, 1.0f, color.getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && isHovering(mouseX, mouseY)) {
            opened = !opened;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && opened)
        {
            for (String string : setting.modes)
            {
                if (mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + 11.0f + height / 2 - (Managers.TEXT.height(true) >> 1) + height - 1)
                {
                    setting.setMode(string);
                }
                y += 11;
            }
        }

        if (opened) return;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY))
            setting.increment();
        else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isHovering(mouseX, mouseY))
            setting.decrement();

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {

    }

    @Override
    public float getHeight() {

        if (opened)
            return height + modeHeight + 2.0f;

        return height;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            animation.reset();
            rect.reset();
        }
    }

    protected boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + height;
    }
}
