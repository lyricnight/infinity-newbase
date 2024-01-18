package club.lyric.infinity.api.util.client.gui.item;

import club.lyric.infinity.api.util.client.gui.ILabel;
import club.lyric.infinity.api.util.client.gui.Mouse;
import club.lyric.infinity.api.util.client.gui.Panel;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.clickgui.GUI;
import club.lyric.infinity.manager.Managers;

import java.awt.*;

public class Button extends Item implements ILabel, IMinecraft {
    private boolean state;

    public Button(String label) {
        super(label);
        this.height = 14;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Rect rect = new Rect(x, y, width, height);
        if (getState()) {
            Render2DUtils.renderRectRollingRainbow(rect, 150);
            Render2DUtils.renderRectOutline(rect, Color.BLACK, 0.1f);
        } else {
            Render2DUtils.renderRect(rect, ColorUtils.newAlpha(Color.BLACK, 60));
        }
        if (rect.doesCollide(new Mouse(mouseX, mouseY))) {
            Render2DUtils.renderRect(rect, ColorUtils.newAlpha(Color.GRAY, 70));
        }
        Managers.TEXT.drawString(getLabel(), rect.getX() + 2f, rect.getY() + 3f, Color.WHITE.getRGB(), true);
        //RenderMethods.drawGradientRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? ClickGui.INSTANCE.getColor(77) : ClickGui.INSTANCE.getColor(55)) : (!this.isHovering(mouseX, mouseY) ? 0x33555555 : 0x77AAAAAB), this.getState() ? (!this.isHovering(mouseX, mouseY) ? ClickGui.INSTANCE.getColor(77) : ClickGui.INSTANCE.getColor(55)) : (!this.isHovering(mouseX, mouseY) ? 0x55555555 : 0x66AAAAAB));

        //mc.fontRenderer.drawString(this.getLabel(), this.x + 2.0f, this.y + 4.0f, this.getState() ? -1 : -5592406, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.state = !this.state;
            this.toggle();
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public float getHeight() {
        return 14;
    }

    protected boolean isHovering(double mouseX, double mouseY) {
        for (Panel panel : GUI.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() + 6.7 && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
