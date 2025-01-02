package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.movement.MovementUtil;
import club.lyric.infinity.impl.events.mc.movement.PlayerMovementEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

/**
 * @author vasler, zenov
 * The strafe is not sixset, but zenov helped me with it.
 * it better not be sixset - lyric
 */
@SuppressWarnings({"unused"})
public final class Speed extends ModuleBase {

    public ModeSetting mode = new ModeSetting("Mode", this, "Strafe", "Strafe", "Jump", "Grim", "StrictFast");
    public BooleanSetting timer = new BooleanSetting("Timer", true, this);
    public BooleanSetting inLiquids = new BooleanSetting("InLiquids", true, this);
    double speed = 0.0;
    double distance = 0.0;
    int stage = 0;
    boolean boost = false;
    double strictTicks;

    public Speed() {
        super("Speed", "Increases your speed.", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        Managers.TIMER.reset();
        stage = 1;
        speed = 0.0f;
        distance = 0.0;
        boost = false;
    }

    @Override
    public String moduleInformation() {
        return mode.getMode();
    }

    @Override
    public void onUpdate() {
        if (mode.is("Jump")) {
            if (!mc.player.isOnGround() || !MovementUtil.movement()) return;

            mc.player.jump();
        }

    }

    @EventHandler
    public void onGang(PlayerMovementEvent event) {

        if (Null.is() || mc.player.isSpectator() || !MovementUtil.movement()) return;

        if (!inLiquids.value() && (mc.player.isInsideWaterOrBubbleColumn() || mc.player.isInLava())) return;

        event.setCancelled(true);


        float defaultSpeed = 0.2873f;

        if (mode.is("Strafe")) {

            if (mc.player.fallDistance <= 5.0 && MovementUtil.movement()) {

                double velocityY = mc.player.getVelocity().y;

                if (stage == 1) {

                    speed *= 1.35f * MovementUtil.calcEffects(2873.0) - 0.01;

                } else if (stage == 2) {

                    velocityY = 0.3999999463558197f;

                    if (boost) {

                        speed *= 1.6835;

                    } else {

                        speed *= 1.395;

                    }

                    boost = !boost;
                } else if (stage == 3) {

                    speed = distance - 0.66 * (distance - MovementUtil.calcEffects(0.2873));

                } else {
                    if ((!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0, mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) && stage > 0) {

                        stage = 1;

                    }

                    speed = distance - distance / 159.0;
                }

                speed = Math.min(speed, MovementUtil.calcEffects(10.0));
                speed = Math.max(speed, MovementUtil.calcEffects(0.2873));

                MovementUtil.strafe(event, speed);

                mc.player.setVelocity(0.0, velocityY, 0.0);
                event.setY(velocityY);
                stage++;

            }
        } else if (mode.is("StrictFast")) {
            if (mc.player.fallDistance <= 5.0 && MovementUtil.movement()) {

                double velocityY = mc.player.getVelocity().y;

                if (stage == 1) {

                    speed *= 1.35f * MovementUtil.calcEffects(2873.0) - 0.01;

                } else if (stage == 2) {

                    velocityY = 0.4000000054314141413434141341431f;

                    if (boost) {

                        speed *= 1.6835;

                    } else {

                        speed *= 1.408;

                    }

                    boost = !boost;
                } else if (stage == 3) {

                    speed = distance - 0.66 * (distance - MovementUtil.calcEffects(0.2873));

                } else {
                    if ((!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0, mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) && stage > 0) {

                        stage = 1;

                    }

                    speed = distance - distance / 159.0;
                }

                strictTicks++;

                speed = Math.min(speed, MovementUtil.calcEffects(10.0));
                speed = Math.max(speed, MovementUtil.calcEffects(0.2873));

                speed = Math.min(speed, strictTicks > 25 ? 0.465 : 0.44);

                if (strictTicks > 50) {
                    strictTicks = 0;
                }

                MovementUtil.strafe(event, speed);

                mc.player.setVelocity(0.0, velocityY, 0.0);
                event.setY(velocityY);
                stage++;

            }
        }
    }

    @Override
    public void onTickPre() {
        if (timer.value()) {
            Managers.TIMER.set(1.0888f);
        } else {
            Managers.TIMER.reset();
        }
        if (Null.is()) return;

        double diffX = mc.player.getX() - mc.player.prevX;
        double diffZ = mc.player.getZ() - mc.player.prevZ;

        distance = Math.sqrt(diffX * diffX + diffZ * diffZ);


        if (mode.is("Grim")) {

            if (MovementUtil.movement()) return;

            int grimCollide = 0;

            for (Entity entity : mc.world.getEntities()) {

                if (entity != mc.player && entity instanceof LivingEntity && MathHelper.sqrt((float) mc.player.squaredDistanceTo(entity)) <= 2.0f) {

                    grimCollide++;

                }

            }

            if (grimCollide > 0) {

                double speed = 0.088888 * grimCollide;
                final Vec2f motion = MovementUtil.strafeSpeed((float) speed);

                mc.player.setVelocity(mc.player.getVelocity().x + motion.x, mc.player.getVelocity().y, mc.player.getVelocity().z + motion.y);

            }
        }
    }
}
