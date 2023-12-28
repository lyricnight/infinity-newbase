package club.lyric.infinity.api.util.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.StringHelper;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.Map;

public class PlayerUtils implements IMinecraft {

    private static final Map<StatusEffect, String> statusEffectNames = new Reference2ObjectOpenHashMap<>(16);

    public static String get(StatusEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringHelper.stripTextFormat(effect1.getName().getString()));
    }
}
