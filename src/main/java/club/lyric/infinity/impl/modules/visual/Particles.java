package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class Particles extends ModuleBase {

    public NumberSetting radius = new NumberSetting("Radius", this, 15f, 1f, 30f, 1f);

    public Particles() {
        super("Particles", "add more particles or change this to a new class", Category.Visual);
    }

    @Override
    public void onUpdate()
    {
        spawnParticles();
    }

    private void spawnParticles()
    {

        ClientWorld world = mc.world;
        Vec3d playerPos = mc.player.getPos();

        for (int i = 0; i < 10; i++)
        {

            double posX = playerPos.x + (mc.player.getRandom().nextDouble() - 0.5) * 2 * radius.getIValue();
            double posY = playerPos.y + (mc.player.getRandom().nextDouble() - 0.5) * 2 * radius.getIValue();
            double posZ = playerPos.z + (mc.player.getRandom().nextDouble() - 0.5) * 2 * radius.getIValue();

            world.addParticle(ParticleTypes.CHERRY_LEAVES, posX, posY, posZ, 0.0, 0.0, 0.0);
        }
    }
}
