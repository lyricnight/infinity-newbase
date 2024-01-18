package club.lyric.infinity.api.util.client.gui.item.settings;

import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.gui.Mouse;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.Button;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.manager.Managers;

import java.awt.*;

@SuppressWarnings("rawtypes")
public class EnumButton extends Button {

    private final Setting<Enum> setting;

    public EnumButton(Setting<Enum> setting) {
        super(setting.getName());
        this.setting = setting;
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
        Managers.TEXT.drawString(String.format("%s\u00a77 %s", this.getLabel(), setting.getValue()), this.x + 2.0f, this.y + 4.0f, this.getState() ? -1 : -5592406, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                increment();
            } else if (mouseButton == 1) {
                decrement();
            }
        }
    }

    @Override
    public float getHeight() {
        return 14f;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
    }

    public void setValue(String value) {
        Enum<?>[] array = setting.getValue().getClass().getEnumConstants();
        for (Enum<?> anEnum : array) {
            if (anEnum.name().equalsIgnoreCase(value)) {
                setting.setValue(anEnum);
                break;  // Exit the loop after setting the value
            }
        }
    }

    public void increment() {
        Enum<?>[] array = setting.getValue().getClass().getEnumConstants();
        Enum<?> currentValue = setting.getValue();
        int currentIndex = getCurrentIndex(array, currentValue);

        int nextIndex = (currentIndex + 1) % array.length;
        setting.setValue(array[nextIndex]);
    }

    public void decrement() {
        Enum<?>[] array = setting.getValue().getClass().getEnumConstants();
        Enum<?> currentValue = setting.getValue();
        int currentIndex = getCurrentIndex(array, currentValue);

        int previousIndex = (currentIndex - 1 + array.length) % array.length;
        setting.setValue(array[previousIndex]);
    }

    private int getCurrentIndex(Enum<?>[] array, Enum<?> currentValue) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == currentValue) {
                return i;
            }
        }
        // Handle the case where the current value is not found in the array.
        return -1;
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }

}
