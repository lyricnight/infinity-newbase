package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.player.InventoryUtils;
import club.lyric.infinity.api.util.render.Render2DUtils;
import club.lyric.infinity.api.util.render.colors.ColorUtils;
import club.lyric.infinity.api.util.render.text.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * @author vasler
 * hud
 * @since a while ago
 */
public class HUD extends ModuleBase {
    public HUD() {
        super("HUD", "Displays HUD elements on the screen.", Category.CLIENT);
    }

    protected int width;
    protected int height;

    @Override
    public void onRender2D(Render2DEvent event) {
        event.getDrawContext().drawText(mc.textRenderer, Infinity.CLIENT_NAME, 2, 2, -1, true);
        renderArmor(event.getDrawContext());
    }

    //TODO: VASLER THIS SPAMS YOUR LOGS WITH ERRORS. FIX IT
    private void renderArmor(DrawContext context)
    {
        width = context.getScaledWindowWidth();
        height = context.getScaledWindowHeight();
        int x = 15;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().armor.get(i);
            if (!stack.isEmpty()) {
                int y = 55;
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
        Render2DUtils.disableStandardItemLighting();
    }

    public float getFixedArmorOffset(float percent) {
        if (percent == 100F) {
            return -0.5F;
        } else if (percent < 10F) {
            return 3.5F;
        } else {
            return 1.5F;
        }
    }


}
