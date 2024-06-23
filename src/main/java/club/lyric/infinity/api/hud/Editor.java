package club.lyric.infinity.api.hud;

import club.lyric.infinity.api.gui.Gui;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class Editor {

    private int x;
    private int y;
    private int x2;
    private int y2;
    public boolean drag;

    public void render(DrawContext context, int mouseX, int mouseY) {
        drag(mouseX, mouseY);
    }


    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovering(mouseX, mouseY))
        {
            x2 = (int) (x - mouseX);
            y2 = (int) (y - mouseY);
            
            drag = true;
        }
    }

    private void drag(int mouseX, int mouseY)
    {
        if (!drag) return;

        x = x2 + mouseX;
        y = y2 + mouseY;
    }
}
