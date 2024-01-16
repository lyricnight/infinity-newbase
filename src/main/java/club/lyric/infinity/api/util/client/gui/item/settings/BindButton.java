package club.lyric.infinity.api.util.client.gui.item.settings;

import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.util.Bind;
import club.lyric.infinity.api.util.client.gui.Mouse;
import club.lyric.infinity.api.util.client.gui.Rect;
import club.lyric.infinity.api.util.client.gui.item.Button;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;

import java.awt.*;

public class BindButton extends Button {
    private final Setting<Bind> setting;
    private boolean binding;

    public BindButton(Setting<Bind> setting) {
        super(setting.getName());
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //??? this may NOt work
        String text = binding ? "Binding..." : setting.getName().concat(": ").concat(setting.getValue().toString());
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
        mc.fontRenderer.drawString(text, this.x + 2.0f, this.y + 4.0f, this.getState() ? -1 : -5592406, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                binding = !binding;
            } else if (mouseButton == 1) {
                binding = false;
                setting.getValue().setKey(-1);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (binding) {
            //might go wrong.
            setting.getValue().setKey(keyCode);
            binding = false;
        }
    }
}
