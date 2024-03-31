package club.lyric.infinity.api.util.client.render.util;


import net.minecraft.util.math.MathHelper;

public final class Easing {

    /**
     * Creates a linear animation, with a consistent delta.
     *
     * @param time     The time passed since the initial starting time of the animation.
     * @param initial  The initial starting float value of the animation.
     * @param target   The target float for the animation to reach.
     * @param duration The duration, in ms, of the animation.
     * @return A linear progression of a float from the initial value to the target value over the course of the duration.
     */
    public static float linear(float time, float initial, float target, float duration) {
        return (time >= duration) ? initial + target : target * time / duration + initial;
    }

    /**
     * Creates an exponential animation, where delta changes from high to low as the animation progresses.
     *
     * @param time     The time passed since the initial starting time of the animation.
     * @param initial  The initial starting float value of the animation.
     * @param target   The target float for the animation to reach.
     * @param duration The duration, in ms, of the animation.
     * @return An exponential progression of a float from the initial value to the target value over the course of the duration.
     */
    public static float exponential(float time, float initial, float target, float duration) {
        return (time >= duration) ? initial + target : target * ((float) -Math.pow(2, -10 * time / duration) + 1) + initial;
    }

    /**
     * Creates an elastic animation, where delta starts off low and increases as the animation progresses, like a rubber band.
     *
     * @param time     The time passed since the initial starting time of the animation.
     * @param initial  The initial starting float value of the animation.
     * @param target   The target float for the animation to reach.
     * @param duration The duration, in ms, of the animation.
     * @return An elastic progression of a float from the initial value to the target value over the course of the duration.
     */
    public static float elastic(float time, float initial, float target, float duration) {
        if (time == 0) return initial;
        if ((time /= duration / 2) == 2) return initial + target;
        float a = 1.0F;
        float s;
        if (a < Math.abs(target)) {
            a = target;
            s = 1.0F / 4.0F;
        } else s = 1.0F / (float) (2 * Math.PI) * (float) Math.asin(target / a);
        if (time < 1)
            return -.5f * (a * (float) Math.pow(2, 10 * (time -= 1)) * MathHelper.sin((float) ((time * duration - s) * (2 * Math.PI)))) + initial;
        return a * (float) Math.pow(2, -10 * (time -= 1)) * MathHelper.sin((float) ((time * duration - s) * (2 * Math.PI))) * .5f + target + initial;
    }

    /**
     * Creates a bouncing animation, where the value slightly overshoots the target value and then reverses direction until it meets the target value.
     *
     * @param time     The time passed since the initial starting time of the animation.
     * @param initial  The initial starting float value of the animation.
     * @param target   The target float for the animation to reach.
     * @param duration The duration, in ms, of the animation.
     * @return A bouncing progression of a float from the initial value to the target value over the course of the duration.
     */
    public static float bounce(float time, float initial, float target, float duration) {
        float s = 1.70158f;
        if (time > duration) {
            return initial + target;
        }
        return target * ((time = time / duration - 1) * time * ((s + 1) * time + s) + 1) + initial;
    }
}