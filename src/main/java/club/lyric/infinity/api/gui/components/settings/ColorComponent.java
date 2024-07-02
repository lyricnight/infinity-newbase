package club.lyric.infinity.api.gui.components.settings;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@SuppressWarnings("ConstantConditions")
public class ColorComponent extends Component implements IMinecraft {

    public ColorSetting setting;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());
    private final Animation alpha = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());

    // height anims
    private final Animation heightButton = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());
    private final Animation heightPicker = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());

    protected StopWatch stopWatch = new StopWatch.Single();
    protected boolean opened = false;
    boolean held;
    float cursorX;
    float cursorY;

    public ColorComponent(ColorSetting setting, Panel panel) {
        this.panel = panel;
        this.setting = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        rect.run(10);

        if (isHovering(mouseX, mouseY)) {
            animation.run(2);
        } else {
            animation.run(0);
        }

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 2.0f, y + 2.0f - animation.getValue(), rect.getValue(), rect.getValue(), setting.getColor().getRGB());
        Render2DUtils.drawOutlineRect(context.getMatrices(), panel.getX() + 2.0f, y + 2.0f - animation.getValue(), rect.getValue(), rect.getValue(), new Color(10, 10, 10, 200).getRGB());

        context.getMatrices().push();
        Managers.TEXT.drawString(setting.getName(), (int) (panel.getX() + width - Managers.TEXT.width(setting.getName(), true)), y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), setting.getColor().getRGB());
        context.getMatrices().pop();

        if (!opened) {
            heightPicker.run(0);
            alpha.run(0);
        } else {
            heightPicker.run(86);
            alpha.run(255);
        }

        float clampX;

        getPicker(context);

        if (opened) {

            for (float i = 0.0f; i < width - 6.0f; i += 0.1f) {
                float hue = i / (width - 6.0f);

                Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 4.0f + i, y + height + 86 + 2.0f, 0.1f, 3.0f, ColorUtils.alpha(new Color(Color.getHSBColor(hue, 1.0f, 1.0f).getRGB()), (int) alpha.getValue()).getRGB());
            }

            Render2DUtils.drawOutlineRect(context.getMatrices(), panel.getX() + 4.0f, y + height + 86 + 2.0f, width - 6.0f, 3, new Color(10, 10, 10).getRGB());

            if (mouseX >= panel.getX() + 4.0f &&
                    mouseX <= panel.getX() + 4.0f + width - 2.0f &&
                    mouseY >= y + height + 86 + 2.0f &&
                    mouseY <= y + height + 86 + 2.0f + 3.0f &&
                    held) {
                clampX = MathHelper.clamp(mouseX, panel.getX() + 4.0f, panel.getX() + 4.0f + width - 2.0f);

                float normalX = (float) normalize(clampX, panel.getX() + 4.0f, panel.getX() + 4.0f + width - 2.0f);

                if (normalX == 1.0f) normalX = 0.990566F;

                setting.setColor(JColor.fromHSB(MathHelper.clamp(normalX, 0.0f, 1.0f), setting.getColor().getHsb()[1], setting.getColor().getHsb()[2]), false);
            }

            Render2DUtils.drawRect(context.getMatrices(), cursorX, cursorY, 4, 4, setting.getColor().getRGB());
            Render2DUtils.drawOutlineRect(context.getMatrices(), cursorX, cursorY, 4, 4, new Color(10, 10, 10).getRGB());

            if (mouseX >= panel.getX() + 2.0f &&
                    mouseX <= panel.getX() + 2.0f + width - 4.0f &&
                    mouseY >= y + height + 2.0f &&
                    mouseY <= y + height + 2.0f + heightPicker.getValue() - 2.0f &&
                    held
            ) {

                float panelX = panel.getX() + 2.0f;
                clampX = MathHelper.clamp(mouseX, panelX, panelX + width - 2.0f);
                float normalX = (float) normalize(clampX, panelX, panelX + width - 2.0f);

                float clampY = MathHelper.clamp(mouseY, y + height + 2.0f, y + height + 88.0f);
                float normalY = (float) normalize(clampY, y + height + 2.0f, y + height + 88.0f);

                normalY = MathHelper.clamp(-normalY + 1.0f, 0.0f, 1.0f);

                setting.setColor(JColor.fromHSB(setting.getColor().getHsb()[0], normalX, normalY), false);

                cursorX = mouseX;
                cursorY = mouseY;

                Render2DUtils.drawRect(context.getMatrices(), mouseX, mouseY, 4, 4, setting.getColor().getRGB());
                Render2DUtils.drawOutlineRect(context.getMatrices(), mouseX, mouseY, 4, 4, new Color(10, 10, 10).getRGB());

            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isHovering(mouseX, mouseY)) {
            opened = !opened;
            heightPicker.reset();
            alpha.reset();
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            held = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            held = false;
        }
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

    @Override
    public float getHeight() {
        if (opened)
            heightButton.run(110);
        else
            heightButton.run(14);

        return heightButton.getValue();
    }

    protected double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    protected void getPicker(DrawContext context) {
        float[] hsb = setting.getColor().getHsb();
        int color = Color.HSBtoRGB(hsb[0], 1.0f, 1.0f);

        float offset = opened ? 2.0f : 0;
        Render2DUtils.drawGradient(context.getMatrices(), panel.getX() + 4.0f, y + height + offset, panel.getX() + width - 2.0f, y + height + heightPicker.getValue(), 0xffffffff, color, true);
        Render2DUtils.drawGradient(context.getMatrices(), panel.getX() + 4.0f, y + height + offset, panel.getX() + width - 2.0f, y + height + heightPicker.getValue(), 0, 0xff000000, false);

        if (opened)
            Render2DUtils.drawOutlineRect(context.getMatrices(), panel.getX() + 4.0f, y + height + offset - 0.5f, 92, heightPicker.getValue() - 2, ColorUtils.alpha(setting.getColor(), (int) alpha.getValue()).getRGB());
    }

}
