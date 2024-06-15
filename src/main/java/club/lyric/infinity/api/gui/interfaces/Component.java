package club.lyric.infinity.api.gui.interfaces;

import club.lyric.infinity.api.gui.Panel;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.gui.DrawContext;

/**
 * @author valser
 */
public abstract class Component implements IMinecraft {

    // Placement
    protected int x;
    protected float y;

    // Size
    protected float width = 100;
    protected float height;
    protected Panel panel;

    // Settings
    protected Setting setting;

    public abstract void mouseClicked(int mouseX, int mouseY, int button);

    public abstract void mouseReleased(int mouseX, int mouseY, int button);

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public abstract void keyPressed(int keyCode, int scanCode, int modifiers);

    public void setY(float y)
    {
        this.y = y;
    }

    public float getHeight()
    {
        return height;
    }
}