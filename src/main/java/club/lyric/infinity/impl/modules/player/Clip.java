package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

/**
 * @author lyric
 * TODO update
 */
public final class Clip extends ModuleBase {
    public ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "NoCheck");
    public NumberSetting delay = new NumberSetting("Delay", this, 3, 0, 20, 1);
    public BooleanSetting disable = new BooleanSetting("Disable", true, this);
    public NumberSetting updates = new NumberSetting("Updates", this, 10, 1, 30, 1);
    int time;

    public Clip() {
        super("Clip", "Clips into blocks.", Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (Null.is()) return;
        if (MovementUtil.movement()) return;

        if (time >= updates.getValue() && disable.value()) {
            setEnabled(false);
        }

        switch (mode.getMode()) {
            case "Normal" -> {
                if (mc.world.getEntityCollisions(mc.player, mc.player.getBoundingBox().expand(0.01, 0, 0.01)).size() < 2) {
                    mc.player.setPosition(MathUtils.roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.301, Math.floor(mc.player.getX()) + 0.699), mc.player.getY(), MathUtils.roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.301, Math.floor(mc.player.getZ())));
                }
                //might be wrong
                else if (mc.player.ticksSinceLastPositionPacketSent % delay.getValue() == 0) {
                    mc.player.setPosition(mc.player.getX() + MathHelper.clamp(MathUtils.roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.241, Math.floor(mc.player.getX()) + 0.759) - mc.player.getX(), -0.03, 0.03), mc.player.getY(), mc.player.getZ() + MathHelper.clamp(MathUtils.roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.241, Math.floor(mc.player.getZ()) + 0.759) - mc.player.getZ(), -0.03, 0.03));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
                    send(new PlayerMoveC2SPacket.PositionAndOnGround(MathUtils.roundToClosest(mc.player.getX(), Math.floor(mc.player.getX()) + 0.23, Math.floor(mc.player.getX()) + 0.77), mc.player.getY(), MathUtils.roundToClosest(mc.player.getZ(), Math.floor(mc.player.getZ()) + 0.23, Math.floor(mc.player.getZ()) + 0.77), true));
                }
            }
            case "NoCheck" -> {
                send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.0042123, mc.player.getZ(), mc.player.isOnGround()));
                send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.02141, mc.player.getZ(), mc.player.isOnGround()));
                //illegal / overflow angles because some servers don't bother checking for them.
                send(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() - 0.097421, mc.player.getZ(), 500, 500, mc.player.isOnGround()));
            }
        }
        time++;
    }

    @Override
    public void onDisable() {
        time = 0;
    }
}
