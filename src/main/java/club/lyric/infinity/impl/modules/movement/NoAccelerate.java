package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.impl.events.mc.movement.EntityMovementEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 */
public final class NoAccelerate extends ModuleBase {
    private boolean pause = false;

    //stupid code, don't want to call through instance everywhere.
    public static boolean pauseGlobal = false;

    public BooleanSetting superStrict = new BooleanSetting("SuperStrict", false, this);

    public NoAccelerate()
    {
        super("NoAccelerate", "Instantly reaches your maximum speed with no acceleration.", Category.MOVEMENT);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onMovement(EntityMovementEvent ignored)
    {
        if (pause || pauseGlobal) return;
        MovementUtil.createSpeed(MovementUtil.getSpeed(true));
    }
    @Override
    public void onUpdate()
    {
        pause = !mc.player.isOnGround() || mc.player.isSpectator() || mc.player.isSneaking() || (superStrict.value() && (Managers.MODULES.getModuleFromClass(Step.class).isOn() || Managers.MODULES.getModuleFromClass(Speed.class).isOn()));
    }

    @Override
    public String moduleInformation()
    {
        if (pause || pauseGlobal)
        {
            return Formatting.RED + "false";
        }
        else
        {
            return Formatting.GREEN + "true";
        }
    }
}