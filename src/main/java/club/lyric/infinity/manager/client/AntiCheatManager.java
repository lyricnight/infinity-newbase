package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.mc.movement.LocationEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import club.lyric.infinity.manager.Managers;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

/**
 * @author lyric
 * declutters the module
 */
public final class AntiCheatManager implements IMinecraft {
    private boolean rotations, strictDirection, movementFix, protocol;
    private float holdingTime;
    private int bpt;

    @EventHandler
    public void onLocation(LocationEvent ignored)
    {
        Managers.MODULES.getModuleFromClass(AntiCheat.class).set();
    }

    public void set(boolean rotations, boolean strictDirection, boolean movementFix, boolean protocol, float holdingTime, int bpt)
    {
        this.rotations = rotations;
        this.strictDirection = strictDirection;
        this.movementFix = movementFix;
        this.protocol = protocol;
        this.holdingTime = holdingTime;
        this.bpt = bpt;
    }

    public float getHoldingTime() {
        return holdingTime;
    }

    public int getBpt() {
        return bpt;
    }

    public boolean isRotations() {
        return rotations;
    }

    public boolean isMovementFix() {
        return movementFix;
    }

    public boolean isProtocol() {
        return protocol;
    }

    public boolean isStrictDirection() {
        return strictDirection;
    }
}
