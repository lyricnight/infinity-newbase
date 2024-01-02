package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.StringHelper;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
}
