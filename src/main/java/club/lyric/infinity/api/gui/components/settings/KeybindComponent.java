package club.lyric.infinity.api.gui.components.settings;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.util.client.keyboard.KeyUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.ClickGui;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("ConstantConditions")
public class KeybindComponent extends Component implements IMinecraft
{

    public BindSetting setting;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGui.class).speed.getLValue());
    private final Animation rect = new Animation(Easing.EASE_OUT_QUAD, Managers.MODULES.getModuleFromClass(ClickGui.class).speed.getLValue());
    protected StopWatch stopWatch = new StopWatch.Single();
    boolean binding = false;
    String commas = "";

    public KeybindComponent(BindSetting setting, Panel panel)
    {
        this.panel = panel;
        this.setting = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {

        if (isHovering(mouseX, mouseY))
        {
            animation.run(2);
        }
        else
        {
            animation.run(0);
        }

        String bind = !binding ? KeyUtils.getKeyName(setting.getCode()) : "Waiting" + listening();

        context.getMatrices().push();
        Managers.TEXT.drawString("Bind: " + bind, (int) (panel.getX() + 4.0f), y + height / 2 - (Managers.TEXT.height(true) >> 1) - animation.getValue(), -1);
        context.getMatrices().pop();

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button)
    {

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY))
        {
            binding = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {

    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            animation.reset();
            rect.reset();
        }

        if (binding)
        {
            if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE)
            {
                keyCode = GLFW.GLFW_RELEASE;
            }

            setting.setCode(keyCode);
            binding = false;
        }
    }

    // stole idea from sexymaster
    public String listening()
    {

        if (stopWatch.hasBeen(250))
        {
            commas = commas.length() >= 3 ? "" : commas + ".";

            stopWatch.reset();
        }

        return commas;
    }
    
    protected boolean isHovering(double mouseX, double mouseY)
    {
        return mouseX >= panel.getX() && mouseX <= panel.getX() + width && mouseY >= y && mouseY <= y + height;
    }
}
