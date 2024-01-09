package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.client.math.Bandhu;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.StringHelper;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.minecraft.util.math.MathHelper.floor;

public class PlayerUtils implements IMinecraft {

    private static final Map<StatusEffect, String> statusEffectNames = new Reference2ObjectOpenHashMap<>(16);

    public static String get(StatusEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringHelper.stripTextFormat(effect1.getName().getString()));
    }

    public static String getPotionDurationString(StatusEffectInstance effect)
    {
        if (effect.isInfinite())
        {
            return "∞∞:∞∞";
        }
        else
        {
            DecimalFormat minuteFormat = new DecimalFormat("0");
            DecimalFormat secondsFormat = new DecimalFormat("00");
            long durationInTicks = effect.getDuration();
            float durationInSeconds = (float)durationInTicks / 20.0F;
            long minutes = TimeUnit.SECONDS.toMinutes((long)durationInSeconds);
            long seconds = TimeUnit.SECONDS.toSeconds((long)durationInSeconds) % 60L;
            String time = minuteFormat.format(minutes) + ":" + secondsFormat.format(seconds);
            return time;
        }
    }

    public static boolean isPhasing() {
        Bandhu bandhu = (Bandhu) mc.player.getBoundingBox();
        for (int x = floor(bandhu.minX); x < floor(bandhu.maxX) + 1; x++) {
            for (int y = floor(bandhu.minY); y < floor(bandhu.maxY) + 1; y++) {
                for (int z = floor(bandhu.minZ); z < floor(bandhu.maxZ) + 1; z++) {
                    if (mc.world.getBlockState(new BlockPos(x, y, z)).blocksMovement()) {
                        if (bandhu.intersects(new Bandhu(x, y, z, x + 1, y + 1, z + 1))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


}
