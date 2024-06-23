package club.lyric.infinity.api.gui.components;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.components.settings.BooleanComponent;
import club.lyric.infinity.api.gui.components.settings.ColorComponent;
import club.lyric.infinity.api.gui.components.settings.KeybindComponent;
import club.lyric.infinity.api.gui.components.settings.SliderComponent;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.client.sounds.SoundsUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGui;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class ModuleComponent extends Component implements IMinecraft
{

    private final ArrayList<Component> components = new ArrayList<>();
    private final ModuleBase moduleBase;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGui.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGui.class).speed.getLValue());
    private final Animation alpha = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGui.class).speed.getLValue());
    float currY;
    boolean open;

    public ModuleComponent(ModuleBase moduleBase, Panel panel)
    {
        this.moduleBase = moduleBase;
        this.panel = panel;

        this.height = 14;

        if (!moduleBase.getSettings().isEmpty())
        {
            // bind
            moduleBase.getSettings().stream()
                    .filter(setting -> setting instanceof BindSetting)
                    .forEach(setting -> components.add(new KeybindComponent((BindSetting) setting, panel)));

            // boolean
            moduleBase.getSettings().stream()
                    .filter(setting -> setting instanceof BooleanSetting)
                    .forEach(setting -> components.add(new BooleanComponent((BooleanSetting) setting, panel)));

            // number
            moduleBase.getSettings().stream()
                    .filter(setting -> setting instanceof NumberSetting)
                    .forEach(setting -> components.add(new SliderComponent((NumberSetting) setting, panel)));

            // colo
            moduleBase.getSettings().stream()
                    .filter(setting -> setting instanceof ColorSetting)
                    .forEach(setting -> components.add(new ColorComponent((ColorSetting) setting, panel)));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {

        alpha.run(200);

        Color color = ColorUtils.alpha(Managers.MODULES.getModuleFromClass(GuiRewrite.class).color.getColor(), (int) alpha.getValue());

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 1.0f, y, width, height, ColorUtils.alpha(color, 70).getRGB());

        if (moduleBase.isOn())
        {
            rect.run(width);
        }
        else if (moduleBase.isOff())
        {
            rect.run(0);
        }

        Render2DUtils.drawRect(context.getMatrices(), panel.getX() + 1.0f, y, rect.getValue(), height, color.getRGB());

        if (isHovering(mouseX, mouseY))
        {
            animation.run(2);
        }
        else
        {
            animation.run(0);
        }

        currY = height;

        context.getMatrices().push();
        Managers.TEXT.drawString(moduleBase.getName(), (int) (panel.getX() + 2.0f), y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), -1);
        context.getMatrices().pop();

        if (!open) return;

        for (Component component : components)
        {
            component.setY(y + currY + 1.0f);
            currY += component.getHeight() + 1.0f;

            component.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers)
    {

        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            alpha.reset();
            animation.reset();
            rect.reset();
        }

        if (!open) return;

        for (Component component : components)
        {
            component.keyPressed(keyCode, scanCode, modifiers);
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY))
        {

            if (moduleBase.isOn())
            {
                SoundsUtils.playSound("disabled.wav", 100);
            }
            else if (moduleBase.isOff())
            {
                SoundsUtils.playSound("enabled.wav", 100);
            }

            moduleBase.toggle();
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isHovering(mouseX, mouseY))
        {
            open = !open;
            return;
        }

        if (!open) return;

        for (Component component : components)
        {
            component.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        if (!open) return;

        for (Component component : components)
        {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public float getHeight()
    {

        if (open)
        {
            float totalHeight = (float) components.stream()
                    .mapToDouble(Component::getHeight)
                    .sum();

            return 14.0f + totalHeight + components.size();
        }

        return 14.0f;
    }

    public String getName()
    {
        return moduleBase.getName();
    }

    protected boolean isHovering(double mouseX, double mouseY)
    {
        return mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + height;
    }
}
