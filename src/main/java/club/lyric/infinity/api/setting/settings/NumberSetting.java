package club.lyric.infinity.api.setting.settings;

import club.lyric.infinity.api.setting.Base;

/**
 * @author lyric
 * numbers...
 */

public class NumberSetting extends Base<Number> {

    public Number increment;
    public Number max;
    public Number min;
    /**
     * might need this?
     */
    private boolean hasIncrement;


    public NumberSetting(Number defaultValue, Number min, Number max, Number inc, String ... stringArray) {
        super(defaultValue, stringArray);
        this.hasIncrement = true;
        this.min = min;
        this.max = max;
        this.increment = inc;
    }

    public NumberSetting(Number defaultValue, Number min, Number max, String ... stringArray) {
        super(defaultValue, stringArray);
        this.hasIncrement = false;
        this.min = min;
        this.max = max;
        this.increment = 0.1;
    }
    @Override
    public Number getValue() {
        return super.getValue();
    }

    public Number getIncrement() {
        return this.increment;
    }

    public Number getMin() {
        return this.min;
    }

    public Number getMax() {
        return this.max;
    }




}
