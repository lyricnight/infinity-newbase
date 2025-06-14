package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import lombok.Getter;

/**
 * @author vasler and lyric
 */
public final class TimerManager implements IMinecraft {
    private float temp;
    @Getter
    private float timer = 1.0f;
    private int ticks = 0;
    private int max;

    public void init() {
        if (mc.player == null) {
            reset();
        } else {
            if (ticks != 0) {
                if (ticks >= max) {
                    reset();
                    ticks = 0;
                    return;
                }
                timer = temp;
                ++ticks;
            }
        }
    }
    public void update()
    {
        if (ticks != 0) {
            if (ticks >= max) {
                reset();
                ticks = 0;
                return;
            }
            timer = temp;
            ++ticks;
        }
    }

    public void unload()
    {
        reset();
    }

    public void set(float timer) {
        this.timer = timer <= 0.0f ? 0.1f : timer;
    }

    public void reset() {
        this.timer = 1.0f;
    }

    public void setFor(float timer, int ticks) {
        this.temp = timer;
        this.max = ticks;
        this.ticks = 1;
    }
}
