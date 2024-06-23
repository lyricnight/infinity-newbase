package club.lyric.infinity.api.gui.components.settings;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGui;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@SuppressWarnings("ConstantConditions")
public class SliderComponent extends Component implements IMinecraft
{

    public NumberSetting  setting;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(GuiRewrite.class).speed.getLValue());
    protected StopWatch stopWatch = new StopWatch.Single();
    public boolean drag;

    public SliderComponent(NumberSetting setting, Panel panel)
    {
        this.panel = panel;
        this.setting = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {

        if (setting.getName().equals("hudX") || setting.getName().equals("hudY")) return;

        Color color = ColorUtils.alpha(Managers.MODULES.getModuleFromClass(GuiRewrite.class).color.getColor(), 200);

        float length = MathUtils.round(((setting.getFValue() - setting.getMinimum()) / (setting.getMaximum() - setting.getMinimum())) * (width - 2));

        rect.run(length);

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 2.0f, y, rect.getValue(), height, color.getRGB());

        if (isHovering(mouseX, mouseY))
        {
            animation.run(2);
        }
        else
        {
            animation.run(0);
        }

        String name = setting.getName() + ": " + String.format("%.2f", setting.getValue()) + (setting.getAppend() != null ? setting.getAppend() : "");
        float textY = y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue();

        context.getMatrices().push();
        Managers.TEXT.drawString(name, (int) (panel.getX() + 4.0f), textY, -1);
        context.getMatrices().pop();

        if (drag)
        {
            double difference = setting.getMaximum() - setting.getMinimum();
            double value = ((mouseX - (panel.getX() + 2)) * difference / (width - 2) + setting.getMinimum());

            setting.setValue(setting.getIncrement() * Math.round(value * (1 / setting.getIncrement())));
        }

        animation.reset();
        rect.reset();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button)
    {

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY))
        {
            drag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        drag = false;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            animation.reset();
            rect.reset();
        }
    }

    protected boolean isHovering(double mouseX, double mouseY)
    {
        return mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + height;
    }
}