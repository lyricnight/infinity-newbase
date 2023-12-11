package club.lyric.infinity.api.util.minecraft;

import net.minecraft.client.MinecraftClient;

/**
 * @author lyric
 * wrapper
 */

public interface IMinecraft {
    public static MinecraftClient mc = MinecraftClient.getInstance();
}
