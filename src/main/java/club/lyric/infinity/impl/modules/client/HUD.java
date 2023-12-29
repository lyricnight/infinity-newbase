package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.player.InventoryUtils;
import club.lyric.infinity.api.util.player.PlayerUtils;
import club.lyric.infinity.api.util.render.colors.ColorUtils;
import club.lyric.infinity.api.util.render.text.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.LinkedList;

/**
 * @author vasler
 * hud
 * @since a while ago
 */
public class HUD extends ModuleBase
{
    public HUD()
    {
        super("HUD", "Displays HUD elements on the screen.", Category.CLIENT);
    }

    protected int width;
    protected int height;
    protected int fpsCount;
    protected final LinkedList<Long> frames = new LinkedList<>();

    @Override
    public void onRender2D(Render2DEvent event)
    {
        int offset = 0;
        event.getDrawContext().drawText(mc.textRenderer, Infinity.CLIENT_NAME, 2, 2, -1, true);

        renderArmor(event.getDrawContext());

        for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects())
        {
            int x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(getString(statusEffectInstance)));
            event.getDrawContext().drawText(mc.textRenderer, getString(statusEffectInstance), x, event.getDrawContext().getScaledWindowHeight() - 9 - offset, statusEffectInstance.getEffectType().getColor(), true);
            offset += 9;
        }

        // FPS starts

        long time = System.nanoTime();

        frames.add(time);

        while (true)
        {

            long f = frames.getFirst();

            final long ONE_SECOND = 1000000L * 1000L;

            if (time - f > ONE_SECOND) frames.remove();

            else break;
        }

        fpsCount = frames.size();

        String fps = "FPS " + fpsCount;

        event.getDrawContext().drawText(mc.textRenderer, fps, event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(fps)), event.getDrawContext().getScaledWindowHeight() - 9 - offset, -1, true);

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
                TextUtils.drawStringWithShadow(context, mc.textRenderer, Text.of(((int) (percent)) + "%"), (int) (((width >> 1) + x + 1 + getFixedArmorOffset(percent)) * 1.333F), (int) ((height - y - 5) * 1.333F), ColorUtils.toColor(percent / 100.0F * 120.0F, 100.0F, 50.0F, 1.0F).getRGB());
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
        String potions = name.getString() + (amplifier > 0 ? (" " + (amplifier + 1) + " ") : " ") + Formatting.WHITE + PlayerUtils.getPotionDurationString(statusEffectInstance);
        return potions;
    }


}
