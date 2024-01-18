package club.lyric.infinity.api.util.client.gui.item;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.settings.BindButton;
import club.lyric.infinity.api.util.client.gui.item.settings.BooleanButton;
import club.lyric.infinity.api.util.client.gui.item.settings.EnumButton;
import club.lyric.infinity.api.util.client.gui.item.settings.NumberSlider;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ModuleButton extends Button implements IMinecraft {
    private final ModuleBase module;
    private final List<Item> items = new ArrayList<>();
    private boolean subOpen;

    public ModuleButton(ModuleBase module) {
        super(module.getName());
        this.module = module;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                items.add(new BooleanButton(setting));
            }
            if (setting instanceof EnumSetting) {
                items.add(new EnumButton(setting));
            }
            if (setting instanceof NumberSetting) {
                items.add(new NumberSlider(setting));
            }
            if (setting instanceof BindSetting) {
                items.add(new BindButton(setting));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Rect rect = new Rect(x, y, width, height);
        Managers.TEXT.drawString(subOpen ? "-" : "+", rect.getX() + rect.getWidth() - 8f, rect.getY() + 3f, Color.WHITE.getRGB(), true);
        AtomicBoolean can = new AtomicBoolean(true);
        items.forEach(item -> {
            if (item instanceof BooleanButton booleanButton) {
                String name = booleanButton.setting.getName();
                can.set(!name.equalsIgnoreCase("drawn") && !name.equalsIgnoreCase("bind"));
            }
        });
        if (!this.items.isEmpty()) {

            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : items) {
                    if (item.isVisible()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                        item.setHeight(15);
                        item.setWidth(getWidth() - 2);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
            }
            if (this.subOpen) {
                for (Item item : items) {
                    if (item.isVisible()) {
                        item.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (!items.isEmpty() && subOpen) {
            for (Item item : items) {
                if (item.isVisible()) {
                    item.keyPressed(keyCode, scanCode, modifiers);
                }
            }
        }
    }

    @Override
    public float getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (Item item : items) {
                if (item.isVisible()) {
                    height += item.getHeight() + 1;
                }
            }
            return height + 2;
        }
        return 14;
    }

    @Override
    public void toggle() {
        module.toggle();
    }

    @Override
    public boolean getState() {
        return module.isOn();
    }
}
