package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.player.Fake;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author lyric
 */
public final class FakePlayer extends ModuleBase {
    public FakePlayer() {
        super("FakePlayer", "Spawns an entity.", Category.Visual);
    }

    @Override
    public void onEnable() {
        if (Null.is()) return;

        Fake fake = new Fake(mc.world);
        fake.setId(-2352352);
        fake.copyPositionAndRotation(mc.player);

        for (int i = 0; i < fake.getInventory().size(); i++) {
            fake.getInventory().setStack(i, mc.player.getInventory().getStack(i));
        }

        mc.world.addEntity(fake);
        fake.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
        fake.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
        fake.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
    }

    @Override
    public void onDisable() {
        if (Null.is()) return;
        mc.world.removeEntity(-2352352, Entity.RemovalReason.DISCARDED);
    }
}
