package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.mc.movement.LocationEvent;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.sun.jna.platform.win32.Variant;

/**
 * @author lyric
 * handling player rotations
 */
public class RotationManager implements IMinecraft {

    /**
     * yaw
     */
    private float yaw;

    /**
     * pitch
     */
    private float pitch;

    /**
     * amount of ticks since infinity has rotated somewhere
     */
    private int ticksSinceRotation;

    /**
     * have we rotated this tick -> for convenience, so you don't have to query tickSinceRotation
     */
    private boolean hasRotated;

    @EventHandler
    public void onLocationPre(LocationEvent.Pre event)
    {
        yaw = mc.player.getYaw();
        pitch = mc.player.getPitch();
    }

    @EventHandler
    public void onLocationPost(LocationEvent.Post event)
    {
        ticksSinceRotation++;

        if (ticksSinceRotation > 2)
        {
            hasRotated = false;
        }

        mc.player.setYaw(yaw);
        mc.player.headYaw = yaw;
        mc.player.setPitch(pitch);
    }


    public void set(float[] rotations) {
        this.set(rotations[0], rotations[1]);
    }

    public void set(float yaw, float pitch) {
        hasRotated = true;
        ticksSinceRotation = 0;
        mc.player.setYaw(yaw);
        mc.player.setHeadYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isRotated() {
        return hasRotated;
    }
}
