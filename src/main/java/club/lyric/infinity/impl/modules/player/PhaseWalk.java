package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.impl.events.mc.movement.SpecificMovementEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * @author lyric
 */
public final class PhaseWalk extends ModuleBase {
    public BooleanSetting sync = new BooleanSetting("Sync", false, this);
    public BooleanSetting resync = new BooleanSetting("ReSync", false, this);

    public BooleanSetting check = new BooleanSetting("Check", false, this);
    public BooleanSetting ongod = new BooleanSetting("OnGround", false, this);
    public BooleanSetting down = new BooleanSetting("Down", false, this);
    public BooleanSetting up = new BooleanSetting("Up", false, this);
    public BooleanSetting busyWait = new BooleanSetting("BusyWait", false, this);
    public BooleanSetting rotationHold = new BooleanSetting("RotationHold", false, this);

    public BooleanSetting timer = new BooleanSetting("Timer", false, this);
    public NumberSetting timerAmount = new NumberSetting("TimerAmount", this, 1.0, 0.0, 10.0, 0.1);
    public NumberSetting pre = new NumberSetting("Pre", this, 5, 0, 20, 1);
    public NumberSetting post = new NumberSetting("Post", this, 5, 0, 20, 1);

    private int phase, rotation, progress, timeLeft, id;

    private boolean goingDown;
    private boolean goingUp;
    private boolean ignore;
    private boolean setTimerToValue;
    private boolean rubberbanded;
    private boolean previous;

    /**
     * representation in double form -> makes it easier
     */
    private double ascendTotal;

    private final StopWatch stopWatch = new StopWatch.Single();

    public PhaseWalk() {
        super("PhaseWalk", "Allows walking through walls.", Category.PLAYER);
    }
    @Override
    public void onDisable() {
        if (setTimerToValue) {
            setTimerToValue = false;
            stopWatch.reset();
        }
        if (!Null.is() && previous && resync.value()) {
            timeLeft = 0;
            phase = 0;
            Vec3d pos = mc.player.getPos();
            send(new PlayerMoveC2SPacket.Full(pos.x, pos.y + 1.0, pos.z, Managers.ROTATIONS.getPrevYaw() + 5.0f, Managers.ROTATIONS.getPrevPitch(), false));
        }
    }

    /**
     * all event methods
     */

    @EventHandler
    public void onPacketReceived(PacketEvent.Receive ignored) {
        goingUp = false;
        goingDown = false;
    }

    @EventHandler
    public void onPacketSent(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket && ((PlayerMoveC2SPacket) event.getPacket()).changesLook() && !ignore) {
            rotation = 0;
        } else if (event.getPacket() instanceof TeleportConfirmC2SPacket) {
            id = ((TeleportConfirmC2SPacket) event.getPacket()).getTeleportId();
        }
    }

    @EventHandler
    public void onMove(SpecificMovementEvent.Pre event) {
        phase(event);
        if (timer.value() && --timeLeft >= 0) {
            setTimerToValue = true;
            Managers.TIMER.set(timerAmount.getFValue());
        } else if (setTimerToValue) {
            setTimerToValue = false;
            Managers.TIMER.reset();
        }
    }

    /**
     * phase methods.
     */

    private void phase(SpecificMovementEvent.Pre event) {
        phase++;
        rotation++;
        boolean waiting = false;
        boolean willDo = BlockUtils.isInside(mc.player, mc.player.getBoundingBox().contract(0.0625));
        if (!willDo) {
            if (previous && rubberbanded && resync.value()) {
                timeLeft = 0;
                phase = 0;
                //might have to use a packet listener here instead, watch out
                Vec3d pos = mc.player.getPos();
                send(new PlayerMoveC2SPacket.Full(pos.x, pos.y + 1.0, pos.z, Managers.ROTATIONS.getPrevYaw() + 5.0f, Managers.ROTATIONS.getPrevPitch(), false));
            }
            rubberbanded = false;
        }
        previous = willDo;
        if (preMovement(event)) {
            return;
        }
        if (rotationHold.value()) {
            waiting = true;
            if (rotation < pre.getValue()) {
                if (busyWait.value()) {
                    mc.player.setVelocity(new Vec3d(0.0, mc.player.getVelocity().y, 0.0));
                    event.setXZ(0.0, 0.0);
                }
                return;
            }
            waiting = false;
            phase = 0;
        }
        handleMovement(event);
    }

    private boolean preMovement(SpecificMovementEvent.Pre event) {
        handleInput();
        if (goingDown) {
            return this.preDescend(event);
        }
        if (goingUp) {
            return this.preAscend(event);
        }
        return preXZ(event);
    }

    private void handleMovement(SpecificMovementEvent.Pre event) {
        if (goingDown) {
            this.handleDescend(event);
        } else if (goingUp) {
            this.handleAscend(event);
        } else {
            this.handleXZ(event);
        }
    }

    private void handleInput() {
        if (goingDown || goingUp) {
            return;
        }
        if (!canVertical()) {
            return;
        }
        if (mc.options.sneakKey.isPressed() && down.value()) {
            goingDown = true;
        } else if (mc.options.jumpKey.isPressed() && up.value()) {
            goingUp = true;
            progress = 0;
            ascendTotal = mc.player.getY();
        }
    }

    private boolean preDescend(SpecificMovementEvent.Pre ignored) {
        return false;
    }

    private boolean preAscend(SpecificMovementEvent.Pre ignored) {
        return false;
    }

    private boolean canVertical() {
        return phased() && isOnGround();
    }

    private boolean preXZ(SpecificMovementEvent.Pre event) {
        Vec3d vec = PlayerUtils.predict(mc.player);
        Vec3d vec2 = mc.player.getPos().add(event.actual);
        double d = vec.subtract(vec2).horizontalLength();
        double d2 = vec.subtract(mc.player.getPos()).horizontalLength();
        if (check.value() && !phased()) {
            return true;
        }
        if (ongod.value() && !this.isOnGround()) {
            return true;
        }
        return d <= 0.01 || d2 >= 0.05;
    }

    private void handleDescend(SpecificMovementEvent.Pre event) {
        Vec3d vec = mc.player.getPos().add(0.0, -0.0253, 0.0);
        sendBounds(vec);
        mc.player.setPosition(vec);
        event.set(0.0, 0.0, 0.0);
        goingDown = false;
    }

    private void handleAscend(SpecificMovementEvent.Pre event) {
        double offset = Math.min(progress * 0.06, 1.0);
        Vec3d vec = mc.player.getPos().withAxis(Direction.Axis.Y, ascendTotal + offset);
        sendBounds(vec);
        mc.player.setPosition(vec);
        event.set(0.0, 0.0, 0.0);
        progress++;
        if (offset >= 1.0) {
            goingUp = false;
        }
    }

    private void handleXZ(SpecificMovementEvent.Pre event) {
        double ratio = event.actual.horizontalLength() / 0.06;
        double x = event.actual.x / ratio;
        double z = event.actual.z / ratio;
        Vec3d vec3 = mc.player.getPos().add(x, event.actual.y, z).withAxis(Direction.Axis.Y, mc.player.getY());
        sendBounds(vec3);
        mc.player.setPosition(vec3);
        event.setXZ(0.0, 0.0);
    }

    private boolean phased() {
        return BlockUtils.isInside(mc.player, mc.player.getBoundingBox().contract(0.07, 0.1, 0.07));
    }

    private boolean isOnGround() {
        return mc.player.isOnGround();
    }

    private void sendBounds(Vec3d to) {
        rubberbanded = true;
        timeLeft = 5;
        send(new PlayerMoveC2SPacket.PositionAndOnGround(to.x, to.y, to.z, mc.player.isOnGround()));
        send(new PlayerMoveC2SPacket.PositionAndOnGround(to.x, to.y - 87.0, to.z, mc.player.isOnGround()));
        send(getIncrement());
        ignore = true;
        if (sync.value()) {
            MovementUtil.sendPositionSync(to, Managers.ROTATIONS.getPrevYaw(), Managers.ROTATIONS.getPrevPitch());
        }
        ignore = false;
    }
    private TeleportConfirmC2SPacket getIncrement() {
        int idLocal = id + 1;
        return new TeleportConfirmC2SPacket(idLocal);
    }

    @Override
    public String moduleInformation()
    {
        return phase + ", " + timeLeft;
    }
}

