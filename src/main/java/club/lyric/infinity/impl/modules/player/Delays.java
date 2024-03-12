package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.ducks.IClientPlayerInteractionManager;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.asm.accessors.IMinecraft;

/**
 * @author lyric
 */
public class Delays extends ModuleBase {
    public BooleanSetting breaking = new BooleanSetting("Breaking", false, this);

    public NumberSetting placing = new NumberSetting("Placing", this, 0, 0, 5, 1);

    public Delays()
    {
        super("Delays", "Manages delays", Category.Player);
    }

    @Override
    public void onTickPre() {
        if (mc.interactionManager != null && breaking.value()) {
            IClientPlayerInteractionManager interactionManager = (IClientPlayerInteractionManager) mc.interactionManager;
            interactionManager.setHitDelay(0);
        }
    }

    @Override
    public void onTickPost()
    {
        if (mc.options.useKey.isPressed() && placing.getIValue() < ((IMinecraft)mc).getItemUseCooldown())
        {
            ((IMinecraft)mc).setItemUseCooldown(placing.getIValue());
        }
    }
}
