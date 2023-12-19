package club.lyric.infinity.api.util.gui;

/**
 * @author valser
 * retard really made a util for 1 method
 */
public class HoveringUtils {
    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
