package club.lyric.infinity.api.hud;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.GuiRewrite;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class HudEditor extends Screen implements IMinecraft
{
    private static ArrayList<Panel> panels = null;

    public HudEditor()
    {
        super(Text.of("Infinity"));

        panels = new ArrayList<>();

        int x = 2;

        for (Category category : Category.values())
        {

            if (category != Category.Hud) return;

            panels.add(new Panel(category, x, 3, true));

            x += 102;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        super.render(context, mouseX, mouseY, delta);
        panels.forEach(panel -> panel.render(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton)
    {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            Managers.MODULES.getModuleFromClass(GuiRewrite.class).setEnabled(false);
            close();
            return true;
        }

        panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static ArrayList<Panel> getPanels()
    {
        return panels;
    }
}