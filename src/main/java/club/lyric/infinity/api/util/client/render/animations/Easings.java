package club.lyric.infinity.api.util.client.render.animations;

import java.util.function.Function;

/**
 * @author vasler
 *
 * Each easing is supposed to return a value which either 0 or 1.
 * <p>
 * Easings taken from <a href="https://easings.net/">...</a>
 */
public enum Easings {
    LINEAR(x -> x),
    LINEAR2(x -> x + 1 - 1);

    private final Function<Double, Double> func;

    Easings(final Function<Double, Double> func) {
        this.func = func;
    }

    public Function<Double, Double> getFuncs() {
        return func;
    }

}
