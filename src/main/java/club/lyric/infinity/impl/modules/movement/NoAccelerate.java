package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.impl.events.mc.movement.EntityMovementEvent;
import net.minecraft.util.Formatting;

/**
 * @author vasler
 */
public class NoAccelerate extends ModuleBase {

    public boolean pause = false;

    public NoAccelerate()
    {
        super("NoAccelerate", "Instantly reaches your maximum speed with no acceleration.", Category.Movement);
    }

    @EventHandler
    public void onMovement(EntityMovementEvent event)
    {
        if (pause) return;
        MovementUtil.createSpeed(MovementUtil.getSpeed(true));
    }
    @Override
    public void onUpdate()
    {
        pause = !mc.player.isOnGround() || mc.player.isSpectator() || mc.player.isSneaking();
    }

    @Override
    public String moduleInformation()
    {
        if (pause)
        {
            return Formatting.RED + "false";
        }
        else
        {
            return Formatting.GREEN + "true";
        }
    }
}