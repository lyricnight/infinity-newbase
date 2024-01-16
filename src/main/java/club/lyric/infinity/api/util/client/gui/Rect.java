package club.lyric.infinity.api.util.client.gui;

/**
 * class representing rects for gui.
 */
public class Rect {

    public float x;

    public float y;

    public float width;

    public float height;

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean doesCollide(Mouse mouse) {
        return mouse.x >= this.getX() && mouse.x <= this.getX() + this.getWidth() && mouse.y >= this.getY() && mouse.y <= this.getY() + this.getHeight();
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
