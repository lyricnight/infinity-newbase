package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.BlockModuleBase;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.entity.EntityUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

/**
 * @author lyric
 * WIP
 */
public final class Burrow extends BlockModuleBase {
    public BooleanSetting strict = new BooleanSetting("Strict", false, this);
    public BooleanSetting disable = new BooleanSetting("Disable", false, this);
    public BooleanSetting center = new BooleanSetting("Center", false, this);
    public ModeSetting offset = new ModeSetting("Offset", this, "Constant", "Constant", "Smart");
    //TODO: dynamic allocation of some sort -> mutable?
    private BlockPos locator = null;
    private final StopWatch.Single stopWatch = new StopWatch.Single();

    public Burrow() {
        super("Burrow", "aids", Category.COMBAT, Integer.MAX_VALUE);
    }

    @Override
    public void onEnable()
    {
        if (Null.is())
        {
            return;
        }
        if (initialCheck())
        {
            Managers.MESSAGES.sendMessage(Formatting.RED + "Burrow cannot place at this time. Disabling...", false);
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
            Managers.MESSAGES.sendMessage(Formatting.RED + "Burrow disabling due to movement.", false);
            toggle();
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
     * this is very stupid but needs to be done this way
     * @return whether we can burrow or not
     */
    private boolean initialCheck()
    {
        return !mc.player.isOnGround() || !BlockUtils.isAir(mc.player.getBlockPos().up(2)) || !canPlace() || !mc.player.verticalCollision;
    }
}
