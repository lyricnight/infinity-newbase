package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.client.math.Time;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author 3arth
 */
public class PlayerPosition extends Vec3d implements IMinecraft {

    private final long timeStamp = Time.getMillis();
    private final float yRot;
    private final float xRot;
    private final boolean onGround;

    public PlayerPosition() {
        this(0.0, 0.0, 0.0, 0.0f, 0.0f, false);
    }

    public PlayerPosition(PlayerPosition position, boolean onGround) {
        this(position.getX(), position.getY(), position.getZ(), position.getXRot(), position.getYRot(), onGround);
    }

    public PlayerPosition(PlayerPosition position, float xRot, float YRot, boolean onGround) {
        this(position.getX(), position.getY(), position.getZ(), xRot, YRot, onGround);
    }

    public PlayerPosition(PlayerPosition position, double x, double y, double z, boolean onGround) {
        this(x, y, z, position.getXRot(), position.getYRot(), onGround);
    }

    public PlayerPosition(double x, double y, double z, float xRot, float yRot, boolean onGround) {
        super(x, y, z);
        this.yRot = yRot;
        this.xRot = xRot;
        this.onGround = onGround;
    }

    public void applyTo(Entity entity) {
        entity.setPos(getX(), getY(), getZ());
        //might be the other way around, mc mappings are fucking annoying
        //TODO: test this
        entity.setPitch(getXRot());
        entity.setYaw(getYRot());
        entity.setOnGround(isOnGround());
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public static PlayerPosition getPosition() {
        //pitch and yaw might be the other way around...
        return new PlayerPosition(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getPitch(), mc.player.getYaw(), mc.player.isOnGround());
    }
}
