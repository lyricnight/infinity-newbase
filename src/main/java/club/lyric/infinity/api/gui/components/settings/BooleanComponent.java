package club.lyric.infinity.api.gui.components.settings;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.client.sounds.SoundsUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGUI;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class BooleanComponent extends Component implements IMinecraft {
    public BooleanSetting setting;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGUI.class).speed.getLValue());

    public BooleanComponent(BooleanSetting setting, Panel panel) {
        this.panel = panel;
        this.setting = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        Color color = ColorUtils.alpha(Managers.MODULES.getModuleFromClass(ClickGUI.class).color.getColor(), 200);

        if (setting.value()) {
            rect.run(width - 2.0f);
        } else if (!setting.value()) {
            rect.run(0);
        }

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 2.0f, y, rect.getValue(), height, color.getRGB());

        if (isHovering(mouseX, mouseY)) {
            animation.run(2);
        } else {
            animation.run(0);
        }

        context.getMatrices().push();
        Managers.TEXT.drawString(setting.getName(), (int) (panel.getX() + 4.0f), y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), -1);
        context.getMatrices().pop();
        animation.reset();
        rect.reset();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY)) {

            if (setting.value()) {
                SoundsUtils.playSound("disabled.wav", 100);
            } else if (!setting.value()) {
                SoundsUtils.playSound("enabled.wav", 100);
            }

            setting.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {

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
