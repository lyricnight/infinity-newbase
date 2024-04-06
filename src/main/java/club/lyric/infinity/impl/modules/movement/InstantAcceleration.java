package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.movement.EntityMovementEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;

@SuppressWarnings({"unused", "ConstantConditions"})
public class InstantAcceleration extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Strict", "Strict", "Normal");

    public InstantAcceleration()
    {
        super("InstantAcceleration", "Instantly accelerates to your maximum speed.", Category.Movement);
    }

    @EventHandler
    public void onMovement(EntityMovementEvent event)
    {
        if (mode.is("Strict"))
        {
            if (!mc.player.isOnGround()) return;

            MovementUtil.directionSpeed(MovementUtil.getSpeed(true));
        }
        if (mode.is("Normal"))
        {
            MovementUtil.directionSpeed(MovementUtil.getSpeed(true));
        }
    }
}
