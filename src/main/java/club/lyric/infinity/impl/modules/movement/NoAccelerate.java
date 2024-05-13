package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.impl.events.mc.movement.EntityMovementEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;

/**
 * @author vasler
 */
@SuppressWarnings({"unused"})
public class NoAccelerate extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Strict", "Strict", "Normal", "Grim");

    public NoAccelerate()
    {
        super("NoAccelerate", "Instantly reaches your maximum speed with no acceleration.", Category.Movement);
    }

    @EventHandler
    public void onMovement(EntityMovementEvent event)
    {
        if (mode.is("Strict"))
        {

            if (!mc.player.isOnGround()) return;

            MovementUtil.createSpeed(MovementUtil.getSpeed(true));

        }

        if (mode.is("Normal"))
        {

            MovementUtil.createSpeed(MovementUtil.getSpeed(true));

        }
    }

    @Override
    public String moduleInformation()
    {
        return mode.getMode();
    }
}
