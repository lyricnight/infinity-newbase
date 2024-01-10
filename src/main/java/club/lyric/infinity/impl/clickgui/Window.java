package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.api.util.client.render.Render2DUtils;
import club.lyric.infinity.api.util.client.render.text.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class Window implements IMinecraft {

    private final Category category;
    private int x;
    private int y;
    private int updatedX;
    private int updatedY;
    protected int width;
    protected int height;
    public boolean drag;

    public Window(Category category, int x, int y, boolean open)
    {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 15;
    }

    private void drag(int mouseX, int mouseY) {
        if (!drag) {
            return;
        }
        this.x = this.updatedX + mouseX;
        this.y = this.updatedY + mouseY;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        drag(mouseX, mouseY);

        int color = -1; // change this color evntaully

        drawCategory(context, color);
    }

    public void drawCategory(DrawContext context, int color)
    {
        Render2DUtils.drawRect(context.getMatrices(), x, y, x + width, y + height, color);
        TextUtils.drawStringWithShadow(context, mc.textRenderer, Text.of(category.toString()), x + 2, y + height / 2, -1);
    }

}
