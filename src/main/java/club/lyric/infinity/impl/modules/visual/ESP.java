package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class ESP extends ModuleBase
{

    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting passive = new BooleanSetting("Passive", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);
    public BooleanSetting items = new BooleanSetting("Items", true, this);
    public BooleanSetting pearls = new BooleanSetting("Pearls", true, this);
    public BooleanSetting box = new BooleanSetting("Box", true, this);
    public BooleanSetting chorus = new BooleanSetting("Chorus", true, this);

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), false);
    BlockPos chorusPos;
    StopWatch.Single stopWatch = new StopWatch.Single();

    public ESP()
    {
        super("ESP", "Communication outside of normal sensory capability, as in telepathy and clairvoyance.", Category.Visual);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {

        for (Entity entity : mc.world.getEntities())
        {

            if (entity instanceof PlayerEntity && entity != mc.player
                    && players.value()
                    || entity instanceof ItemEntity
                    && items.value()
                    || entity instanceof EnderPearlEntity
                    && pearls.value()
                    || entity instanceof AnimalEntity
                    || entity instanceof PassiveEntity
                    && passive.value()
                    || entity instanceof MobEntity
                    && mobs.value()
            )
            {

                Vec3d vec3D = Interpolation.interpolateEntity(entity);

                renderBox(matrixStack, Interpolation.interpolatedBox(entity, vec3D));

            }

        }

        if (chorus.value() && chorusPos != null)
        {
            if (stopWatch.hasBeen(2500))
            {
                chorusPos = null;
                return;
            }

            renderBox(matrixStack, new Box(chorusPos.toCenterPos(), chorusPos.toCenterPos()));
        }

    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event)
    {

        if (event.getPacket() instanceof PlaySoundS2CPacket sound)
        {

            if (sound.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || sound.getSound() == SoundEvents.ENTITY_ENDERMAN_TELEPORT)
            {

                chorusPos = new BlockPos((int) sound.getX(), (int) sound.getY(), (int) sound.getZ());
                stopWatch.reset();

            }

        }

    }

    public void renderBox(MatrixStack matrixStack, Box bb)
    {

        Render3DUtils.enable3D();
        matrixStack.push();

        if (box.value())
        {
            Render3DUtils.drawBox(matrixStack, bb, new Color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), 76).getRGB());
        }

        Render3DUtils.drawOutline(matrixStack, bb, color.getColor().getRGB());

        matrixStack.pop();
        Render3DUtils.disable3D();

    }


}
