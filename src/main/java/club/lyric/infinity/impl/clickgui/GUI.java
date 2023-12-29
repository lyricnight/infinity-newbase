package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.api.module.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends Screen
{

    private final ArrayList<Window> windows = new ArrayList<>();

    public GUI()
    {
        super(Text.of("GUI"));
    }

    @Override
    protected void init()
    {
        int x = 5;
        for (final Category category : Category.values())
        {
            windows.add(new Window(category, x, 5, true));
            x += 110;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(0, 0, 0, 120).getRGB());
    }

    @Override
    public boolean shouldPause()
    {
        return false;
    }

}
