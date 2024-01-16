package club.lyric.infinity.api.util.client.gui.item;

import club.lyric.infinity.api.util.client.gui.ILabel;

/**
 * class representing items.
 */
public class Item implements ILabel {
    private final String label;
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public Item(String label) {
        this.label = label;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void drawScreen(int mouseX, int mouseY, float delta) {
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    }

    public void mouseReleased(double mouseX, double mouseY, int releaseButton) {
    }

    @Override
    public final String getLabel() {
        return this.label;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void init() {}

    public void keyPressed(int keyCode, int searchCode, int modifier) {

    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
