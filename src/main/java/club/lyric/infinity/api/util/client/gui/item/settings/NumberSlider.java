package club.lyric.infinity.api.util.client.gui.item.settings;

import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.gui.Mouse;
import club.lyric.infinity.api.util.client.gui.Panel;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.Item;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.clickgui.GUI;


import java.awt.*;

public class NumberSlider extends Item implements IMinecraft {

    private final Setting<Number> setting;
    private final Number min;
    private final Number max;
    private final int difference;

    public NumberSlider(Setting<Number> setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = setting.getMin();
        this.max = setting.getMax();
        this.difference = max.intValue() - min.intValue();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragSetting(mouseX, mouseY);
        Rect rect = new Rect(x, y, width, height);
        Render2DUtils.renderRectRollingRainbow(rect, 150);
        Render2DUtils.renderRectOutline(rect, Color.BLACK, 0.1f);
        if (rect.doesCollide(new Mouse(mouseX, mouseY))) {
            Render2DUtils.renderRect(rect, ColorUtils.newAlpha(Color.GRAY, 70));
        }
        rect.setWidth(setting.getValue().floatValue() <= min.floatValue() ? x : x + (width + 7.4F) * partialMultiplier());
        mc.fontRenderer.drawString(String.format("%s\u00a77 %s", this.getLabel(), this.setting.getValue()), this.x + 2.0f, this.y + 4.0f, -1, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            setSettingFromX(mouseX);
        }
    }

    private void setSettingFromX(int mouseX) {
        float percent = (mouseX - x) / (width + 7.4F);
        if (setting.getValue() instanceof Double) {
            double result = (double) setting.getMin() + (difference * percent);
            setting.setValue(Math.round(10.0 * result) / 10.0);
        } else if (setting.getValue() instanceof Float) {
            float result = (float) setting.getMin() + (difference * percent);
            setting.setValue(Math.round(10.0f * result) / 10.0f);
        } else if (setting.getValue() instanceof Integer) {
            setting.setValue(((int) setting.getMin() + (int) (difference * percent)));
        }
    }

    @Override
    public float getHeight() {
        return 14f;
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (isHovering(mouseX, mouseY) && Mouse) {
            setSettingFromX(mouseX);
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        for (Panel panel : GUI.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() + 7.4F && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }

    private float getValueWidth() {
        return ((Number) this.setting.getMax()).floatValue() - ((Number) this.setting.getMax()).floatValue() + ((Number) this.setting.getValue()).floatValue();
    }

    private float middle() {
        return max.floatValue() - min.floatValue();
    }

    private float part() {
        return ((Number) setting.getValue()).floatValue() - min.floatValue();
    }

    private float partialMultiplier() {
        return part() / middle();
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
