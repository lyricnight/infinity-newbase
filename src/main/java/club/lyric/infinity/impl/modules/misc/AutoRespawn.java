package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.client.gui.screen.DeathScreen;

/**
 * @author vasler
 */
public class AutoRespawn extends ModuleBase {
    public AutoRespawn()
    {
        super("AutoRespawn", "what do you think idiot", Category.Misc);
    }
    @Override
    public void onUpdate()
    {
        if (mc.player.isDead()) mc.player.requestRespawn();
        if (mc.currentScreen instanceof DeathScreen) mc.player.requestRespawn();
    }
}
