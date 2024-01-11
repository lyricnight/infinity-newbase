package club.lyric.infinity.api.event.mc.movement;

import club.lyric.infinity.api.event.Event;

/**
 * @author lyric
 * event when client player movement is called
 */
public class MotionEvent extends Event {
    private double x;

    private double y;

    private double z;

    public boolean modified;

    private final int stage;

    public MotionEvent(double x, double y, double z, int stage)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.stage = stage;
    }


    public boolean isModified() {
        return modified;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.modified = true;
        this.x = x;
    }

    public void setY(double y)
    {
        this.modified = true;
        this.y = y;
    }

    public void setZ(double z) {
        this.modified = true;
        this.z = z;
    }

    public int getStage() {
        return stage;
    }
}
