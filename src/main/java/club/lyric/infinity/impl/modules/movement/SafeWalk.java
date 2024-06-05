package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SafeWalk extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Legit", "Legit", "Normal");
    public SafeWalk()
    {
        super("SafeWalk", "Removes clipping once you reach a ledge.", Category.Movement);
    }

    @Override
    public void onUpdate()
    {
        if (nullCheck() || !mode.is("Legit")) return;


        KeyBinding.setKeyPressed(mc.options.sneakKey.getDefaultKey(), mc.player.isOnGround() && isAboveAir());
    }

    // me erry night
    /**public boolean edging(PlayerEntity player)
    {

        if (player.isTouchingWater() || player.isInFluid() || player.getAbilities().flying || player.isClimbing()) return false;

        double x = player.getX() - Math.floor(player.getX());
        double z = player.getZ() - Math.floor(player.getZ());

        double threshold = 0.1;

        if (!player.isOnGround()) return true;

        return (x < threshold || x > 1 - threshold) || (z < threshold || z > 1 - threshold);
    }*/

    public boolean isAboveAir() {
        World world = mc.world;
        BlockPos blockPos = new BlockPos((int) mc.player.getX(), (int) (mc.player.getY() - 1.0), (int) mc.player.getZ());
        return world.isAir(blockPos);
    }
}
