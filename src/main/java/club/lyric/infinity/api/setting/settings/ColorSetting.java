package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImInt;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * color picker.
 */
public class ColorSetting extends Setting implements Renderable {
    private JColor color;
    private final ArrayList<Renderable> renderableSettings = new ArrayList<>();
    private final boolean alpha;
    private boolean showSliders;
    @Getter
    private boolean rainbow;

    public ColorSetting(String name, ModuleBase moduleBase, JColor color, boolean alpha) {
        this.name = name;
        this.moduleBase = moduleBase;
        this.color = color;
        this.alpha = alpha;
        this.rainbow = false;

        renderableSettings.addAll(List.of(
                new BooleanRainbowSetting("Rainbow", moduleBase.getName() + "/" + this.getName() + "/Rainbow", rainbow),
                new ColorSliderSetting("Red", moduleBase.getName() + "/" + this.getName() + "/Red", color.getRed()),
                new ColorSliderSetting("Green", moduleBase.getName() + "/" + this.getName() + "/Green", color.getGreen()),
                new ColorSliderSetting("Blue", moduleBase.getName() + "/" + this.getName() + "/Blue", color.getBlue())));

        if (alpha)
            renderableSettings.add(new ColorSliderSetting("Alpha", moduleBase.getName() + "/" + this.getName() + "/Alpha", color.getAlpha()));

        moduleBase.addSettings(this);
    }

    private static class ColorSliderSetting extends NumberSetting {
        private final String imGuiID;

        public ColorSliderSetting(String name, String imGuiID, int value) {
            super(name, null, value, 0, 255, 1);
            this.imGuiID = imGuiID;
        }

        @Override
        public void render() {
            ImGui.pushID(imGuiID);

            ImGui.text(this.name);

            ImInt val = new ImInt((int) this.getValue());

            ImGui.pushItemWidth(160f);
            boolean changed = ImGui.sliderScalar("", ImGuiDataType.S32, val, (int) getMinimum(), (int) getMaximum());
            ImGui.popItemWidth();

            if (changed) this.setValue(val.doubleValue());

            ImGui.popID();
        }
    }

    private static class BooleanRainbowSetting extends BooleanSetting {
        private final String imGuiID;

        public BooleanRainbowSetting(String name, String imGuiID, boolean value) {
            super(name, value, null);
            this.imGuiID = imGuiID;
        }

        @Override
        public void render() {
            ImGui.pushID(imGuiID);

            ImGui.text(this.name);
            if (ImGui.checkbox("", this.value())) {
                toggle();
            }

            ImGui.popID();
        }
    }

    public JColor getValue() {
        return color;
    }

    public JColor getColor() {
        if (rainbow) return getRainbow(0, this.getValue().getAlpha());
        return color;
    }

    public static JColor getRainbow(int incr, int alpha) {
        JColor color = JColor.fromHSB(((System.currentTimeMillis() + incr * 200) % (360 * 20)) / (360f * 20), 0.5f, 1f);
        return new JColor(color.getRed(), color.getBlue(), color.getGreen(), alpha);
    }

    public void setColor(JColor color, boolean rainbow) {
        this.color = color;
        this.rainbow = rainbow;

        ((BooleanSetting) renderableSettings.get(0)).setValue(rainbow);
        ((ColorSliderSetting) renderableSettings.get(1)).setValue(color.getRed());
        ((ColorSliderSetting) renderableSettings.get(2)).setValue(color.getGreen());
        ((ColorSliderSetting) renderableSettings.get(3)).setValue(color.getBlue());
        if (alpha) ((ColorSliderSetting) renderableSettings.get(4)).setValue(color.getAlpha());
    }

    @Override
    public void render() {
        ImGui.pushID(moduleBase.getName() + "/" + this.getName());

        float[] color = getColor().getFloatColorWAlpha();

        ImGui.text(this.getName());

        if (ImGui.colorButton(this.getName(), color)) showSliders = !showSliders;

        if (showSliders) {
            ImGui.indent(10f);

            for (Renderable renderable : renderableSettings) {
                renderable.render();
            }

            ImGui.unindent(10f);

            if (alpha) {
                this.setColor(new JColor(
                                ((ColorSliderSetting) renderableSettings.get(1)).getIValue(),
                                ((ColorSliderSetting) renderableSettings.get(2)).getIValue(),
                                ((ColorSliderSetting) renderableSettings.get(3)).getIValue(),
                                ((ColorSliderSetting) renderableSettings.get(4)).getIValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).value());
            } else {
                this.setColor(new JColor(
                                ((ColorSliderSetting) renderableSettings.get(1)).getIValue(),
                                ((ColorSliderSetting) renderableSettings.get(2)).getIValue(),
                                ((ColorSliderSetting) renderableSettings.get(3)).getIValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).value());
            }

            ImGui.spacing();
        }

        ImGui.popID();
    }
}
