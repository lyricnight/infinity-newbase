package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.client.option.Perspective;

public final class Freelook extends ModuleBase {

    float lastPitch;
    float lastYaw;

    public Freelook() {
        super("Freelook", "Allows you to look around your surroundings", Category.Visual);
    }

    @Override
    public void onEnable() {
        lastPitch = mc.player.getPitch();
        lastYaw = mc.player.getYaw();
    }

    @Override
    public void onUpdate() {
        mc.player.bodyYaw = 0;
        mc.player.renderYaw = 0;

        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }
}
