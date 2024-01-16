package club.lyric.infinity.api.util.client.gui;

/**
 * class representing mouse position.
 */
public class Mouse {

    public final double x;

    public final double y;

    public Mouse(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

}
