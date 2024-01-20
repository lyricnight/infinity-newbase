package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.RenderableSetting;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import imgui.ImGui;

import java.util.ArrayList;

public class ColorSetting extends Setting<JColor> implements RenderableSetting {

    public boolean init;

    private final ArrayList<RenderableSetting> renderableSettings = new ArrayList<>();

    private final boolean alpha;

    private boolean showSliders;

    private boolean rainbow;


    public ColorSetting(String name, JColor color, boolean alpha, String description)
    {
        super(name, color, description);

        this.alpha = alpha;
        this.showSliders = false;
        this.rainbow = false;
    }

    public void init()
    {
        BooleanRainbowSetting rainbowSetting = module.createBoolRainbow(new BooleanRainbowSetting("Rainbow", module.getName() + "/" + getName() + "/Rainbow", rainbow, "Whether to render a rainbow or not."));
        ColorSliderSetting red = module.createColorSlider(new ColorSliderSetting("Red", module.getName() + "/" + getName() + "/Red", getValue().getRed(), "Red amount"));
        ColorSliderSetting blue = module.createColorSlider(new ColorSliderSetting("Blue", module.getName() + "/" + getName() + "/Blue", getValue().getBlue(), "Blue amount"));
        ColorSliderSetting green = module.createColorSlider(new ColorSliderSetting("Green", module.getName() + "/" + getName() + "/Green", getValue().getBlue(), "Green amount"));

        renderableSettings.add(rainbowSetting);
        renderableSettings.add(red);
        renderableSettings.add(blue);
        renderableSettings.add(green);

        if (alpha)
        {
            ColorSliderSetting alphaSetting = module.createColorSlider(new ColorSliderSetting("Alpha", module.getName() + "/" + getName() + "/Alpha", getValue().getAlpha(), "Alpha."));
            renderableSettings.add(alphaSetting);
        }
        init = true;
    }

    public boolean isRainbow() {
        return rainbow;
    }


    public static JColor getRainbow(int incr, int alpha) {
        JColor color =  JColor.fromHSB(((System.currentTimeMillis() + incr * 200) % (360 * 20)) / (360f * 20),0.5f,1f);
        return new JColor(color.getRed(), color.getBlue(), color.getGreen(), alpha);
    }

    public void setColor(JColor color, boolean rainbow) {
        if(!init)
        {
            init();
        }
        this.value = color;
        this.rainbow = rainbow;

        ((BooleanSetting) renderableSettings.get(0)).setValue(rainbow);
        ((ColorSliderSetting) renderableSettings.get(1)).setValue(color.getRed());
        ((ColorSliderSetting) renderableSettings.get(2)).setValue(color.getGreen());
        ((ColorSliderSetting) renderableSettings.get(3)).setValue(color.getBlue());
        if (alpha) ((ColorSliderSetting) renderableSettings.get(4)).setValue(color.getAlpha());
    }

    @Override
    public void render() {
        if(!init)
        {
            init();
        }
        ImGui.pushID(module.getName() + "/" + getName());

        float[] color = getValue().getFloatColorWAlpha();

        ImGui.text(getName());

        if (ImGui.colorButton(getName(), color)) showSliders = !showSliders;

        if (showSliders) {
            ImGui.indent(10f);

            for (RenderableSetting renderableSetting : renderableSettings) {
                renderableSetting.render();
            }

            ImGui.unindent(10f);

            if (alpha) {
                this.setColor(new JColor(
                                ((ColorSliderSetting) renderableSettings.get(1)).getValue(),
                                ((ColorSliderSetting) renderableSettings.get(2)).getValue(),
                                ((ColorSliderSetting) renderableSettings.get(3)).getValue(),
                                ((ColorSliderSetting) renderableSettings.get(4)).getValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).getValue());
            } else {
                this.setColor(new JColor(
                                ((ColorSliderSetting) renderableSettings.get(1)).getValue(),
                                ((ColorSliderSetting) renderableSettings.get(2)).getValue(),
                                ((ColorSliderSetting) renderableSettings.get(3)).getValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).getValue());
            }

            ImGui.spacing();
        }

        ImGui.popID();
    }
}
