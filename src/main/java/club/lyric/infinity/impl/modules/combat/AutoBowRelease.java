package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBowRelease extends ModuleBase {

    public NumberSetting delay = new NumberSetting("Delay", this, 15.0f, 0.0f, 1000.0f, 0.1f, "ms");

    private final StopWatch.Single timer = new StopWatch.Single();

    public AutoBowRelease()
    {
        super("AutoBowRelease", "aa", Category.Combat);
    }


    @Override
    public void onUpdate()
    {
        if (mc.player.isHolding(Items.BOW) && mc.options.useKey.isPressed() && timer.hasBeen(delay.getLValue()) && mc.player.getItemUseTime() > 2)
        {
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            timer.reset();
        }
    }

}
