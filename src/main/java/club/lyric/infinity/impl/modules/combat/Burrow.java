package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.BlockModuleBase;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

/**
 * @author lyric
 */
public final class Burrow extends BlockModuleBase {
    public BooleanSetting strict = new BooleanSetting("Strict", false, this);
    public BooleanSetting persistent = new BooleanSetting("Persistent", false, this);
    public BooleanSetting center = new BooleanSetting("Center", false, this);

    public ModeSetting offset = new ModeSetting("Offset", this, "Constant", "Constant", "Smart");
    //TODO: dynamic allocation of some sort -> mutable?
    private BlockPos locator = null;
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    public Burrow() {
        super("Burrow", "aids", Category.Combat, Integer.MAX_VALUE);
    }

    @Override
    public void onEnable()
    {
        if (initialCheck())
        {
            ChatUtils.sendMessagePrivate(Formatting.RED + "Burrow cannot place at this time. Disabling...");
            toggle();
            return;
        }
        locator = new BlockPos(EntityUtils.getPlayerPos());
    }

    @Override
    public void onUpdate()
    {
        if (!checkValidPos())
        {
            ChatUtils.sendMessagePrivate(Formatting.RED + "Burrow disabling due to movement.");
            toggle();
            return;
        }

    }

    /**
     * check that must be run after initialCheck()
     * @return whether the position we will burrow is valid.
     */
    private boolean checkValidPos()
    {
        return locator.equals(EntityUtils.getPlayerPos()) && mc.world.getBlockState(locator).isReplaceable();
    }

    /**
     * convenience
     * @return whether we can burrow or not
     */
    private boolean initialCheck()
    {
        return Null.is() || !mc.player.isOnGround() || !BlockUtils.isAir(mc.player.getBlockPos().up(2)) || !canPlace() || !mc.player.verticalCollision;
    }
}
