package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.math.Null;
import net.minecraft.client.option.KeyBinding;

/**
 * @author vasler
 */
public final class Sprint extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Rage", "Rage", "Legit");

    public Sprint() {
        super("Sprint", "Sprints for you", Category.Movement);
    }

    @Override
    public void onUpdate() {
        if (Null.is()) return;

        if (mc.player.getHungerManager().getFoodLevel() <= 6.0F || mc.player == null || mc.player.isSneaking()) return;

        if (mode.is("Rage")) {
            if (mc.options.forwardKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed() || mc.options.backKey.isPressed()) {
                mc.player.setSprinting(true);
            }
        } else if (mode.is("Legit")) {
            KeyBinding.setKeyPressed(mc.options.sprintKey.getDefaultKey(), true);
        }
    }

    @Override
    public String moduleInformation() {
        return mode.getMode();
    }
}
