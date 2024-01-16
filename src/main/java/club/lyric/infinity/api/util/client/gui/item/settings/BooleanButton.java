package club.lyric.infinity.api.util.client.gui.item.settings;

import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.gui.Mouse;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.Button;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;

import java.awt.*;

public class BooleanButton extends Button {

    public Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Rect rect = new Rect(x, y, width, height);
        if (getState()) {
            Renderer.renderRectRollingRainbow(rect, 150);
            Renderer.renderRectOutline(rect, Color.BLACK, 0.1f);
        } else {
            Renderer.renderRect(rect, ColorUtils.newAlpha(Color.BLACK, 60));
        }
        if (rect.doesCollide(new Mouse(mouseX, mouseY))) {
            Renderer.renderRect(rect, ColorUtils.newAlpha(Color.GRAY, 70));
        }
        mc.fontRenderer.drawString(this.getLabel(), this.x + 2.0f, this.y + 4.0f, this.getState() ? -1 : -5592406, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public float getHeight() {
        return 14f;
    }

    @Override
    public void toggle() {
        this.setting.setValue(!setting.getValue());
    }

    @Override
    public boolean getState() {
        return setting.getValue();
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
