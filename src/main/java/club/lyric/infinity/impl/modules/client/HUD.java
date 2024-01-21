package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.LinkedList;

/**
 * @author vasler
 * hud
 * @since a while ago
 */
public class HUD extends ModuleBase
{
    public BooleanSetting fps = new BooleanSetting("FPS", true, this);

    public BooleanSetting tps = new BooleanSetting("TPS", true, this);

    public BooleanSetting ping = new BooleanSetting("Ping", true, this);

    public BooleanSetting potions = new BooleanSetting("Potions", true, this);

    public BooleanSetting armorHud =
            new BooleanSetting(
                    "ArmorHUD",
                    true,
                    this
            );

    public BooleanSetting watermark =
            new BooleanSetting(
                    "Watermark",
                    true,
                    this
            );

    public BooleanSetting speed =
            new BooleanSetting(
                    "Speed",
                    true,
                    this
            );


    public HUD()
    {
        super("HUD", "Displays HUD elements on the screen.", Category.Client);
    }

    protected int width;
    protected int height;
    protected int fpsCount;
    protected final LinkedList<Long> frames = new LinkedList<>();

    @Override
    public void onRender2D(Render2DEvent event)
    {
        int offset = 0;

        if (watermark.value())
        {
            event.getDrawContext().drawText(mc.textRenderer, Infinity.CLIENT_NAME, 2, 2, -1, true);
        }

        if (armorHud.value())
        {
            renderArmor(event.getDrawContext());
        }

        if (potions.value())
        {
            for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                int x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(getString(statusEffectInstance))) - 2;
                event.getDrawContext().drawText(mc.textRenderer, getString(statusEffectInstance), x, event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2, statusEffectInstance.getEffectType().getColor(), true);
                offset += 9;
            }
        }

        // Speed starts
        if (speed.value())
        {
            double distanceX = mc.player.getX() - mc.player.prevX;
            double distanceZ = mc.player.getZ() - mc.player.prevZ;

            String speed = "Speed: " + Formatting.WHITE + MathUtils.roundFloat((MathHelper.sqrt((float) (Math.pow(distanceX, 2) + Math.pow(distanceZ, 2))) / 1000) / (0.05F / 3600), 2) + " km/h";

            event.getDrawContext().drawText(mc.textRenderer, speed, event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(speed)) - 2, event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2, -1, true);
            offset += 9;
        }
        // Speed ends

        // TPS starts
        if (tps.value())
        {
            String tps = "TPS: " + Formatting.WHITE + Managers.SERVER.getOurTPS();

            event.getDrawContext().drawText(mc.textRenderer, tps, event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(tps)) - 2, event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2, -1, true);
            offset += 9;
        }
        // TPS ends

        // TPS starts
        if (ping.value())
        {
            String ping = "Ping: " + Formatting.WHITE + Managers.SERVER.getFastLatencyPing();

            event.getDrawContext().drawText(mc.textRenderer, ping, event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(ping)) - 2, event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2, -1, true);
            offset += 9;
        }
        // TPS ends

        // FPS starts
        if (fps.value())
        {
            long time = System.nanoTime();

            frames.add(time);

            while (true) {

                long f = frames.getFirst();

                final long ONE_SECOND = 1000000L * 1000L;

                if (time - f > ONE_SECOND) frames.remove();

                else break;
            }

            fpsCount = frames.size();

            String fps = "FPS: " + Formatting.WHITE + fpsCount;

            event.getDrawContext().drawText(mc.textRenderer, fps, event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(fps)) - 2, event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2, -1, true);
            offset += 9;
        }
        // FPS ends
    }

    private void renderArmor(DrawContext context)
    {
        width = context.getScaledWindowWidth();
        height = context.getScaledWindowHeight();
        int x = 15;
        for (int i = 3; i >= 0; i--)
        {
            ItemStack stack = mc.player.getInventory().armor.get(i);
            if (!stack.isEmpty())
            {
                int y;
                if (mc.player.isInsideWaterOrBubbleColumn() && mc.player.getAir() > 0 && !mc.player.isCreative())
                {
                    y = 65;
                }
                    else
                {
                    y = mc.player.isCreative() ? (mc.player.isRiding() ? 45 : 38) : 55;
                }
                final float percent = InventoryUtils.getPercent(stack);
                context.getMatrices().push();
                context.getMatrices().scale(0.75F, 0.75F, 1F);
                RenderSystem.disableDepthTest();
                Managers.TEXT.drawString(((int) (percent) + "%"), (int) (((width >> 1) + x + 1 + getFixedArmorOffset(percent)) * 1.333F), (int) ((height - y - 5) * 1.333F), ColorUtils.toColor(percent / 100.0F * 120.0F, 100.0F, 50.0F, 1.0F).getRGB(), true);
                RenderSystem.enableDepthTest();
                context.getMatrices().scale(1.0F, 1.0F, 1.0F);
                context.getMatrices().pop();
                context.getMatrices().push();
                context.drawItemInSlot(mc.textRenderer, stack, width / 2 + x, height - y);
                context.drawItem(stack, width / 2 + x, height - y);
                context.getMatrices().pop();
                x += 18;
            }
        }
    }

    public float getFixedArmorOffset(float percent)
    {
        if (percent == 100F) {
            return -0.5F;
        } else if (percent < 10F) {
            return 3.5F;
        } else {
            return 1.5F;
        }
    }


    private String getString(StatusEffectInstance statusEffectInstance)
    {
        int amplifier = statusEffectInstance.getAmplifier();
        Text name = statusEffectInstance.getEffectType().getName();
        return name.getString() + (amplifier > 0 ? (" " + (amplifier + 1) + ": ") : ": ") + Formatting.WHITE + PlayerUtils.getPotionDurationString(statusEffectInstance);
    }
}
