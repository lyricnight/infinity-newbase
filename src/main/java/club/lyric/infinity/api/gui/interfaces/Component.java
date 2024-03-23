package club.lyric.infinity.api.gui.interfaces;

import club.lyric.infinity.api.setting.Setting;
import net.minecraft.client.gui.DrawContext;

public abstract class Component {

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
}