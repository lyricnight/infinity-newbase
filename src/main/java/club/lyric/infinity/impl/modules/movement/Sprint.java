package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

public class Sprint extends ModuleBase
{
    public Sprint()
    {
        super("Sprint", "Sprints for you", Category.MOVEMENT);
    }

    @Override
    public void onUpdate()
    {
        if (mc.player.getHungerManager().getFoodLevel() <= 6.0F || mc.player == null || mc.player.isSneaking())
        {
            return;
        }
        mc.player.setSprinting(true);
    }
}
