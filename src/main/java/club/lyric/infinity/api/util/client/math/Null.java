package club.lyric.infinity.api.util.client.math;

import club.lyric.infinity.api.util.minecraft.IMinecraft;

/**
 * @author lyric
 * checks for nulls
 */
public final class Null implements IMinecraft {
    public static boolean is() {
        return mc.player == null || mc.world == null;
    }
}
