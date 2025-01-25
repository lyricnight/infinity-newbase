package club.lyric.infinity.manager.fabric;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.api.util.minecraft.rotation.RotationPoint;
import club.lyric.infinity.impl.events.mc.movement.LocationEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.impl.events.render.RenderPlayerModelEvent;
import club.lyric.infinity.impl.modules.exploit.Interpolation;
import club.lyric.infinity.manager.Managers;
import lombok.Getter;
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
//TODO: delete all of this. its literally garbage what did i think i was doing with this
public final class RotationManager implements IMinecraft {

    /**
     * previous and current rotation values.
     */
    @Getter
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
    @Getter
    private boolean rotating;

    /**
     * again for convenience
     */
    private int rotatedTicks;

    /**
     * this updates our rotation values
     * @implNote we can't put this inside the mixin, as it's not the most prioritised usage of packetEvent.
     * @param event event duh
     */
    @SuppressWarnings("unused")
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
     * :brain: - lyric
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
        }
        if(current != null && current.isInstant())
        {
            current = null;
        }
    }

    @EventHandler
    public void onRenderPlayerModel(RenderPlayerModelEvent event) {
        if (event.getEntity() == mc.player && current != null) {
            if (!Managers.MODULES.getModuleFromClass(Interpolation.class).isOn())
            {
                event.setYaw(club.lyric.infinity.api.util.client.render.util.Interpolation.interpolateFloat(prevYaw, serverYaw, mc.getRenderTickCounter().getTickDelta(false)));
                event.setPitch(club.lyric.infinity.api.util.client.render.util.Interpolation.interpolateFloat(prevPitch, serverPitch, mc.getRenderTickCounter().getTickDelta(false)));
            }
            prevYaw = event.getYaw();
            prevPitch = event.getPitch();
            event.setCancelled(true);
        }
    }

    /**
     * method used to deliver submissions
     * @param rotationPoint - point to rotate to.
     */
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
     * used when we don't want a rotationPoint
     */
    public void setRotationSilently(float yaw, float pitch)
    {
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
    }

    public void sync()
    {
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
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

    private boolean isHoldingTimeFinished()
    {
        return rotatedTicks > Managers.ANTICHEAT.getHoldingTime();
    }
}
