package club.lyric.infinity.api.util.minecraft.rotation;

/**
 * @author lyric
 * handles rotations as an interface.
 * state -> whether the rotation is pre or post interact
 * angles -> self-explanatory
 */
public interface RotationHandler {
    void handle(boolean state, float[] angles);
}
