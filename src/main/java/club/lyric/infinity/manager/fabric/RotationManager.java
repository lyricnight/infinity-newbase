package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.client.math.Null;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.impl.events.mc.movement.LocationEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.impl.events.render.RenderPlayerModelEvent;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author lyric
 * handling player rotations
 */
//TODO: long in the future, try to make this threaded? have some sort of .submit( -> ) runnable that executes constant rotationPoint submissions?
public final class RotationManager implements IMinecraft {

    /**
     * previous and current rotation values.
     */
    private float serverYaw, serverPitch, prevYaw, prevPitch;

    /**
     * list of rotating points to complete.
     * maybe thread this somehow?
     * some sort of service that does these but synced?
     */
    private final List<RotationPoint> points = new ArrayList<>();

    /**
     * represents the point we are currently rotating to (for convenience)
     */
    @Nullable
    private RotationPoint current;

    /**
     * if we need to rotate or not
     */
    private boolean rotating;

    /**
     * again for convenience
     */
    private int rotatedTicks;

    /**
     * this updates our rotation values
     * @param event event duh
     */
    @EventHandler(priority = Integer.MAX_VALUE - 2)
    public void onPacketSend(PacketEvent.Send event)
    {
        if (Null.is()) return;
        if (event.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket && playerMoveC2SPacket.changesLook())
        {
            serverYaw = playerMoveC2SPacket.getYaw(0.0f);
            serverPitch = playerMoveC2SPacket.getPitch(0.0f);
        }
    }

    /**
     * called before modules receive onUpdate, to ensure this has priority before they affect rotations.
     */
    public void update() {
        if (points.isEmpty()) {
            current = null;
            return;
        }
        //most important rotationPoint
        RotationPoint prioritisedRotation = getPrioritisedRotation();
        //null check for @Nullable
        if (prioritisedRotation == null && isHoldingTimeFinished()) {
            current = null;
            return;
        }
        else if (prioritisedRotation != null)
        {
            current = prioritisedRotation;
        }
        if (current == null)
        {
            return;
        }
        rotatedTicks = 0;
        rotating = true;
    }

    @EventHandler(priority = Integer.MAX_VALUE - 1)
    public void onLocation(LocationEvent event)
    {
        if (current != null && rotating)
        {
            points.remove(current);
            event.setCancelled(true);
            event.setYaw(current.getYaw());
            event.setPitch(current.getPitch());
            rotating = false;
            //checks for instant rotation
            if (current.getInstant())
            {
                current = null;
            }
        }
    }

    @EventHandler
    public void onRenderPlayerModel(RenderPlayerModelEvent event) {
        if (event.getEntity() == mc.player && current != null) {
            event.setYaw(Interpolation.interpolateFloat(prevYaw, serverYaw, mc.getTickDelta()));
            event.setPitch(Interpolation.interpolateFloat(prevPitch, serverPitch, mc.getTickDelta()));
            prevYaw = event.getYaw();
            prevPitch = event.getPitch();
            event.setCancelled(true);
        }
    }

    public void setRotationPoint(RotationPoint rotationPoint)
    {
        //instant for max value
        if (rotationPoint.getPriority() == Integer.MAX_VALUE)
        {
            current = rotationPoint;
        }
        RotationPoint toAdd = points.stream().filter(rp -> rotationPoint.getPriority() == rp.getPriority()).findFirst().orElse(null);
        if (toAdd == null)
        {
            points.add(rotationPoint);
        }
        else
        {
            toAdd.setYaw(rotationPoint.getYaw());
            toAdd.setPitch(rotationPoint.getPitch());
        }
    }

    /**
     * woo!
     * @return most prioritised rotationPoint
     */
    private RotationPoint getPrioritisedRotation()
    {
        Optional<RotationPoint> rotationPoint = points.stream().max(Comparator.comparingInt(RotationPoint::getPriority));
        return rotationPoint.orElse(null);
    }

    /**
     * idk
     * @return - if a rotationPoint will have to queue
     */

    public boolean isRotationLate(int priority)
    {
        return current != null && current.getPriority() > priority;
    }

    public boolean isRotating()
    {
        return rotating;
    }

    private boolean isHoldingTimeFinished()
    {
        return rotatedTicks > AntiCheat.getHoldingTime();
    }
}
