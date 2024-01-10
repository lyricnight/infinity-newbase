package club.lyric.infinity.api.setting;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.client.SettingEvent;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.util.Bind;
import club.lyric.infinity.api.setting.settings.util.EnumConverter;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author lyric
 * @param <T> capture of ? for our setting.
 */

public class Setting<T> {
    private final String name;
    private final T defaultValue;
    public T value;
    public T plannedValue;
    public T min;
    public T max;
    private boolean hasRestriction;
    private Predicate<T> visibility;
    private final String description;

    public ModuleBase module;

    public Setting(String name, T defaultValue, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = description;
    }

    public Setting(String name, T defaultValue, T min, T max, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = description;
        this.hasRestriction = true;
    }

    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.visibility = visibility;
        this.description = description;
        this.hasRestriction = true;
    }

    public Setting(String name, T defaultValue, Predicate<T> visibility, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility = visibility;
        this.plannedValue = defaultValue;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.setPlannedValue(value);
        if (this.hasRestriction) {
            if (((Number) this.min).floatValue() > ((Number) value).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number) this.max).floatValue() < ((Number) value).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        SettingEvent event = new SettingEvent();
        EventBus.getInstance().post(event);
        if (!event.isCancelled()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public void reset() {
        setValue(getDefaultValue());
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public void setPlannedValue(T value) {
        this.plannedValue = value;
    }

    public T getMin() {
        return this.min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return this.max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public void setValueNoEvent(T value) {
        this.setPlannedValue(value);
        if (this.hasRestriction) {
            if (((Number) this.min).floatValue() > ((Number) value).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number) this.max).floatValue() < ((Number) value).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        this.value = this.plannedValue;
    }

    public int getEnum(String input) {
        for (int i = 0; i < this.value.getClass().getEnumConstants().length; ++i) {
            Enum e = (Enum) this.value.getClass().getEnumConstants()[i];
            if (!e.name().equalsIgnoreCase(input)) continue;
            return i;
        }
        return -1;
    }

    public void setEnumValue(String value) {
        for (Enum e : ((Enum) this.value).getClass().getEnumConstants()) {
            if (!e.name().equalsIgnoreCase(value)) continue;
            this.value = (T) e;
        }
    }

    public String currentEnumName() {
        return EnumConverter.getProperName((Enum) this.value);
    }

    public int currentEnum() {
        return EnumConverter.currentEnum((Enum) this.value);
    }

    public void increaseEnum() {
        this.plannedValue = (T) EnumConverter.increaseEnum((Enum) this.value);
        SettingEvent event = new SettingEvent();
        EventBus.getInstance().post(event);
        if (!event.isCancelled()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public void increaseEnumNoEvent() {
        this.value = (T) EnumConverter.increaseEnum((Enum) this.value);
    }

    public String getType() {
        if (this.isEnumSetting()) {
            return "Enum";
        }
        return this.getClassName(this.defaultValue);
    }

    public ModuleBase getModule()
    {
        return module;
    }

    public void setModule(ModuleBase module)
    {
        this.module = module;
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public String getDescription() {
        return Objects.requireNonNullElse(this.description, "");
    }

    public boolean isNumberSetting() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Bind) && !(this.value instanceof Character) && !(this.value instanceof Boolean);
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public void setVisibility(Predicate<T> visibility) {
        this.visibility = visibility;
    }

    public boolean isVisible() {
        if (this.visibility == null) {
            return true;
        }
        return this.visibility.test(this.getValue());
    }
}

