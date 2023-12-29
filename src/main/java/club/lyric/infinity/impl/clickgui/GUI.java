package club.lyric.infinity.impl.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class GUI extends Screen
{

    public GUI()
    {
        super(Text.of("GUI"));
    }

    @Override
    protected void init()
    {

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
