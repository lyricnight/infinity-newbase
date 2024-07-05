package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.client.option.KeyBinding;

// i need this for traveling lyric dontdelete
public class AutoWalk extends ModuleBase
{

    public AutoWalk()
    {
        super("AutoWalk", "aaa", Category.Movement);
    }

    @Override
    public void onUpdate()
    {
        KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), true);
    }
}
