package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.block.hole.Hole;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.api.util.minecraft.rotation.RotationUtils;
import club.lyric.infinity.impl.events.mc.movement.EntityMovementEvent;
import club.lyric.infinity.impl.modules.movement.Step;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

/**
 * @author ??
 */
@SuppressWarnings({"DataFlowIssue", "unused"})
public final class HoleSnap extends ModuleBase {
    public NumberSetting range = new NumberSetting("Range", this, 4f, 1f, 10f, 0.5f, "m");
    public BooleanSetting step = new BooleanSetting("Step", false, this);
    public BooleanSetting remove = new BooleanSetting("Remove", false, this);
    public NumberSetting timerLength = new NumberSetting("Length", this, 20, 1, 100, 5);
    public NumberSetting timerIntensity = new NumberSetting("Intensity", this, 25.0f, 1.0f, 100.0f, 1.0f);
    public NumberSetting postTimerLength = new NumberSetting("PostLength", this, 0, 0, 40, 1);
    public NumberSetting postTimerIntensity = new NumberSetting("PostIntensity", this, 1f, 0.1f, 1f, 0.1f);
    public BooleanSetting extraSafe = new BooleanSetting("ExtraSafe", false, this);
    public BooleanSetting terrain = new BooleanSetting("Terrain", false, this);
    private final StopWatch.Single boostWatch = new StopWatch.Single();
    private Hole hole;
    private int local;
    private int stuck;
    private int boosted;

    public HoleSnap() {
        super("HoleSnap", "Snaps into holes.", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        if (local == 0) {
            boostWatch.reset();
        }
        local += 5;
        if (step.value()) {
            Managers.MODULES.getModuleFromClass(Step.class).setEnabled(true);
        }
        hole = getTarget(range.getFValue());
        if (hole == null) {
            ChatUtils.sendMessagePrivate("HoleSnap failed to find a hole. Disabling.");
            setEnabled(false);
            return;
        }
        if (hole.getFirst().getY() >= mc.player.getY() && !step.value()) {
            ChatUtils.sendMessagePrivate("HoleSnap failed to find a hole below your Y-Level. Disabling.");
            setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        if (step.value()) {
            Managers.MODULES.getModuleFromClass(Step.class).setEnabled(false);
        }
        Managers.TIMER.reset();
    }

    @Override
    public void onTickPre() {
        if (Null.is()) {
            setEnabled(false);
            return;
        }
        if (boostWatch.hasBeen(15000L)) {
            boostWatch.reset();
            local = 0;
        }
        if (mc.player.isSpectator()) {
            setEnabled(false);
            return;
        }
        if (boosted <= timerLength.getValue()) {
            if (remove.value()) {
                //might not work properly
                Managers.TIMER.set(Math.max(timerIntensity.getFValue() - local, 1.0f));
            } else {
                Managers.TIMER.set(timerIntensity.getFValue());
            }
            ++boosted;
        } else {
            boosted = 0;
            Managers.TIMER.reset();
        }
    }

    @EventHandler
    public void onMove(EntityMovementEvent event) {
        if (Null.is()) {
            return;
        }
        if (mc.player.isSpectator()) {
            return;
        }
        if (hole == null || !BlockUtils.isAir(hole.getFirst())) {
            setEnabled(false);
            return;
        }
        if (isSafe()) {
            Managers.TIMER.setFor(postTimerIntensity.getFValue(), postTimerLength.getIValue());
            setEnabled(false);
        }
        execute(event);
        if (mc.player.horizontalCollision && mc.player.isOnGround()) {
            ++this.stuck;
            if (this.stuck == 10) {
                setEnabled(false);
            }
        } else {
            this.stuck = 0;
        }
    }


    private void execute(EntityMovementEvent event) {
        double speed;
        Vec3d playerPos = mc.player.getPos();
        Vec3d holePos = HoleUtils.getCenter(hole);
        Vec3d targetPos = new Vec3d(holePos.x, mc.player.getY(), holePos.z);
        double yawRad = Math.toRadians(RotationUtils.getRotationAsVec2f(playerPos, targetPos).x);
        double dist = playerPos.distanceTo(targetPos);
        double d = speed = mc.player.isOnGround() ? -Math.min(0.2805, dist / 2.0) : -PlayerUtils.getSpeed(mc.player) + 0.02;
        if (dist < 0.1) {
            event.setX(0.0);
            event.setZ(0.0);
            return;
        }
        event.setX(-Math.sin(yawRad) * speed);
        event.setZ(Math.cos(yawRad) * speed);
    }


    private boolean isSafe() {
        double dist = mc.player.getPos().distanceTo(HoleUtils.getCenter(hole));
        return HoleUtils.isInHole(mc.player) || dist < 0.1;
    }

    private boolean isReachable(Hole hole) {
        if (extraSafe.value()) {
            for (int i = 1; i <= 5; ++i) {
                if (!BlockUtils.isAir(hole.getFirst().up(i))) {
                    return false;
                }
                if (hole.getSecond() == null || BlockUtils.isAir(hole.getSecond().up(i))) continue;
                return false;
            }
        }
        return !(hole.getFirst().getY() >= mc.player.getY()) || step.value();
    }

    private Hole getTarget(float range) {
        return HoleUtils.getHoles(range, true, false, false, terrain.value()).stream().filter(this::isReachable).filter(hole -> mc.player.getPos().distanceTo(new Vec3d(hole.getFirst().getX() + 0.5, mc.player.getY(), hole.getFirst().getZ() + 0.5)) <= range).min(Comparator.comparingDouble(hole -> mc.player.getPos().distanceTo(new Vec3d(hole.getFirst().getX() + 0.5, mc.player.getY(), hole.getFirst().getZ() + 0.5)))).orElse(null);
    }
}
