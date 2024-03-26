package club.lyric.infinity.api.gui.interfaces;

import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.gui.DrawContext;

/**
 * @author valser
 */
public abstract class Component implements IMinecraft {

    // Placement
    protected float x;
    protected float y;

    // Size
    protected float width;
    protected float height;

    // Settings
    protected Setting setting;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
    }

    public abstract void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks);

    protected boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}