package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.option.Perspective;

public final class Freelook extends ModuleBase {

    float lastPitch;
    float lastYaw;

    public Freelook() {
        super("Freelook", "Allows you to look around your surroundings", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        lastPitch = mc.player.getPitch();
        lastYaw = mc.player.getYaw();
    }

    @Override
    public void onUpdate() {
        Managers.ROTATIONS.setRotationPoint(new RotationPoint(lastYaw, lastPitch, 9999, true));

        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }
}
