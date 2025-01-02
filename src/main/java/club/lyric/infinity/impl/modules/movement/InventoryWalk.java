package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * @author vasler
 */

public final class InventoryWalk extends ModuleBase {

    public InventoryWalk()
    {
        super("InventoryWalk", "AA", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {

        if (mc.currentScreen == null || mc.currentScreen instanceof ChatScreen) return;

        KeyBinding[] moving = { mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey };

        for (KeyBinding bind : moving)
        {
            KeyBinding.setKeyPressed(bind.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), bind.getDefaultKey().getCode()));
        }
    }
}
