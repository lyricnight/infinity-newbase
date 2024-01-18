package club.lyric.infinity.api.util.client.gui;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.client.gui.item.Button;
import club.lyric.infinity.api.util.client.gui.item.Item;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.clickgui.GUI;
import club.lyric.infinity.impl.modules.client.ClickGui;
import club.lyric.infinity.manager.Managers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class representing Panels for gui.
 */
public abstract class Panel implements ILabel, IMinecraft {

    private final Category category;
    private float x2;
    private float y2;
    private boolean open;
    public boolean drag;
    private final List<Item> items = new ArrayList<>();
    private final Rect rect;
    private final Rect categoryRect;

    public Panel(Category category, int x, int y, boolean open) {
        this.category = category;
        this.rect = new Rect(x, y, 100, 16);
        this.categoryRect = new Rect(x - 2, y - 18, 104, 18);
        this.open = open;
        this.setupItems();
    }

    public abstract void setupItems();

    public void init() {
        getItems().forEach(Item::init);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        /*if (ClickGui.INSTANCE.animation.getValue()) {
            glPushMatrix();
            glEnable(GL_SCISSOR_TEST);
            glViewport(0, 0, resolution.getScaledWidth() * 2, resolution.getScaledHeight() * 2);
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            float progress = (float) elapsedTime / ClickGui.INSTANCE.animationTime.getValue().intValue();
            int scissorY = (int) (resolution.getScaledHeight() - progress * resolution.getScaledHeight());
            glScissor(0, scissorY * 2, resolution.getScaledWidth() * 2, (resolution.getScaledHeight() - scissorY) * 2);
            glPushAttrib(GL_SCISSOR_BIT);
        }
        this.drag(mouseX, mouseY);
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        RenderMethods.drawGradientRect(this.x, (float) this.y - 1.5f, this.x + this.width, this.y + this.height - 6, ClickGui.INSTANCE.getColor(77), ClickGui.INSTANCE.getColor(77));//0x77FB4242, 0x77FB4242);
        if (this.open) {
            RenderMethods.drawRect(this.x, (float) this.y + 12.5f, this.x + this.width, this.open ? (float) (this.y + this.height) + totalItemHeight : (float) (this.y + this.height - 1), 0x77000000);//1996488704
        }
        mc.fontRenderer.drawString(this.getLabel(), (float) this.x + 3.0f, (float) this.y + 1.5f, -1, true);

        if (!open) {
            if (this.angle > 0) {
                this.angle -= 6;
            }
        } else if (this.angle < 180) {
            this.angle += 6;
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
        ColorUtil.glColor(new Color(255, 255, 255, 255));
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/comus/arrow.png"));
        GlStateManager.translate(getX() + getWidth() - 9, (getY() + 6) - 0.3F, 0.0F);
        GlStateManager.rotate(calculateRotation(angle), 0.0F, 0.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(-5, -5, 0.0F, 0.0F, 10, 10, 10, 10, 10.0F, 10.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if (this.open) {
            float y = (float) (this.getY() + this.getHeight()) - 3.0f;
            for (Item item : getItems()) {
                item.setLocation((float) this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += (float) item.getHeight() + 1.5f;
            }
        }
        if (ClickGui.INSTANCE.animation.getValue()) {
            glPopAttrib();
            glDisable(GL_SCISSOR_TEST);
            glPopMatrix();
        }*/

        if (this.open) {
            //replace with setting
            renderBody(Color.MAGENTA);
            float y = rect.getY();
            for (Item item : getItems()) {
                item.setLocation(rect.getX(), y);
                item.setWidth(rect.getWidth());
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += item.getHeight() + 1f;
            }
        }
        rect.setHeight(getTotalItemHeight());

        renderCategory(Color.WHITE, Managers.MODULES.getModuleFromClass(ClickGui.class).shadow.getValue());
        this.drag(mouseX, mouseY);
    }

    public void renderBody(Color color) {
        Render2DUtils.renderRect(rect, color);
        Render2DUtils.renderRectOutline(rect, color, 0.1f);
        Rect bottomRect = new Rect(rect.getX(), rect.getY() + rect.getHeight() - 2.5f, rect.getWidth(), 2);
        Render2DUtils.renderRectRollingRainbow(bottomRect, 255);
        Render2DUtils.renderRectOutline(bottomRect, color, 0.1f);
    }

    public void renderCategory(Color color, boolean shadow) {
        Render2DUtils.renderRectRollingRainbow(categoryRect, 255);
        Render2DUtils.renderRectOutline(categoryRect, color, 0.1f);
        float x = MathUtils.getMiddle(categoryRect.getX(), categoryRect.getWidth(), Managers.TEXT.width(mc.textRenderer, category.name(), Managers.MODULES.getModuleFromClass(ClickGui.class).shadow.getValue()));
        float y = MathUtils.getMiddle(categoryRect.getY(), categoryRect.getHeight(), Managers.TEXT.height(mc.textRenderer, Managers.MODULES.getModuleFromClass(ClickGui.class).shadow.getValue()));
        Managers.TEXT.drawString(category.name(), x, y, Color.WHITE.getRGB(), shadow);
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        categoryRect.setX(x2 + mouseX);
        categoryRect.setY(y2 + mouseY);
        rect.setX(x2 + mouseX + 2);
        rect.setY(y2 + mouseY + 18);
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && categoryRect.doesCollide(new Mouse(mouseX, mouseY))) {
            x2 = (float) (categoryRect.getX() - mouseX);
            y2 = (float) (categoryRect.getY() - mouseY);
            GUI.getClickGui().getPanels().forEach(panel -> {
                if (panel.drag) {
                    panel.drag = false;
                }
            });
            drag = true;
            return;
        }
        if (mouseButton == 1 && categoryRect.doesCollide(new Mouse(mouseX, mouseY))) {
            open = !open;
            return;
        }
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        getItems().forEach(item -> item.keyPressed(keyCode, scanCode, modifiers));
    }

    public void addButton(Button button) {
        items.add(button);
    }

    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
        if (releaseButton == 0) {
            drag = false;
        }
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public final String getLabel() {
        return category.name();
    }

    public float getX() {
        return rect.getX();
    }

    public float getY() {
        return rect.getY();
    }

    public float getWidth() {
        return rect.getWidth();
    }

    public float getHeight() {
        return rect.getHeight();
    }

    public final List<Item> getItems() {
        return this.items;
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : getItems()) {
            height += item.getHeight() + 1.5f;
        }
        return height;
    }

    public void setX(float dragX) {
        rect.setX(dragX);
    }

    public void setY(float dragY) {
        rect.setY(dragY);
    }
}

