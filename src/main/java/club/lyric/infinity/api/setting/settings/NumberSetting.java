package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;

/**
 * @author lyric
 */
public class NumberSetting extends Setting {
    private double value;
    private double minimum;
    private double maximum;
    private double increment;
    private final boolean decimal;
    private String append;

    public NumberSetting(String name, ModuleBase moduleBase, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.moduleBase = moduleBase;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.decimal = !(Math.floor(increment) == increment);

        if (moduleBase != null) moduleBase.addSettings(this);
    }

    public NumberSetting(String name, ModuleBase moduleBase, double value, double minimum, double maximum, double increment, String append) {
        this.name = name;
        this.moduleBase = moduleBase;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = 1.0f;
        this.decimal = !(Math.floor(increment) == increment);
        this.append = append;

        if (moduleBase != null) moduleBase.addSettings(this);
    }

    public double getValue() {
        return this.value;
    }


    public String getAppend() {
        return append;
    }

    public float getFValue() {
        return (float) this.value;
    }

    public long getLValue() {
        return (long) this.value;
    }

    public int getIValue() {
        return (int) this.value;
    }

    public void setValue(double value) {
        double precision = 1.0D / this.increment;
        this.value = Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
    }

    public void increment(boolean positive) {
        setValue(getValue() + (positive ? 1 : -1) * this.increment);
    }

    public double getMinimum() {
        return this.minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getIncrement() {
        return this.increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}