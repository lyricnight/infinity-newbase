package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.RenderableSetting;
import club.lyric.infinity.api.setting.Setting;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImDouble;
import imgui.type.ImInt;
import io.netty.channel.nio.AbstractNioChannel;

import java.util.function.Predicate;

/**
 * @author bel
 * Use this instead of floatsettings, intsettings and doublesettings? (Much easier to work with.)
 * @param <T>
 */
public class NumberSetting<T extends Number> extends Setting<T> implements RenderableSetting {
    private final boolean clamp;

    public NumberSetting(String name, T defaultValue, T min, T max, String description) {
        super(name, defaultValue, description);
        clamp = true;
        this.min = min;
        this.max = max;
    }

    public NumberSetting(String name, T defaultValue, T min, T max, Predicate<T> visibility , String description) {
        super(name, defaultValue, visibility, description);
        clamp = true;
        this.min = min;
        this.max = max;
    }
    
    @Override
    public void setValue(Number value) {
        if (clamp & (max != null && min != null)) {
            if (value instanceof Integer) {
                if (value.intValue() > max.intValue()) {
                    value = max;
                } else if (value.intValue() < min.intValue()) {
                    value = min;
                }
            } else if (value instanceof Float) {
                if (value.floatValue() > max.floatValue()) {
                    value = max;
                } else if (value.floatValue() < min.floatValue()) {
                    value = min;
                }
            } else if (value instanceof Double) {
                if (value.doubleValue() > max.doubleValue()) {
                    value = max;
                } else if (value.doubleValue() < min.doubleValue()) {
                    value = min;
                }
            } else if (value instanceof Long) {
                if (value.longValue() > max.longValue()) {
                    value = max;
                } else if (value.longValue() < min.longValue()) {
                    value = min;
                }
            } else if (value instanceof Short) {
                if (value.shortValue() > max.shortValue()) {
                    value = max;
                } else if (value.shortValue() < min.shortValue()) {
                    value = min;
                }
            } else if (value instanceof Byte) {
                if (value.byteValue() > max.byteValue()) {
                    value = max;
                } else if (value.byteValue() < min.byteValue()) {
                    value = min;
                }
            }
        }
        super.setValue((T) value);
    }


    @Override
    public void render()
    {
        ImGui.pushID(module.getName()+"/"+this.getName());

        ImGui.text(getName());
        boolean changed;

        if (!(value instanceof Integer)) {
            ImDouble val = new ImDouble((double) value);

            ImGui.pushItemWidth(170f);
            changed = ImGui.sliderScalar("", ImGuiDataType.Double, val, (double) min, (double) max, "%.3f");
            ImGui.popItemWidth();

            if (changed) setValue(val.doubleValue());
        } else {
            ImInt val = new ImInt((int) value);

            ImGui.pushItemWidth(170f);
            changed = ImGui.sliderScalar("", ImGuiDataType.S32, val, (int) min, (int) max);
            ImGui.popItemWidth();

            if (changed)
                //might be val.doubleValue()
                setValue(val.intValue());
        }

        ImGui.popID();
    }
}