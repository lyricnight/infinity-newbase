package club.lyric.infinity.api.gui;

/**
 * @author lyric
 * for anything that can be rendered in gui.
 */
public interface RenderableElement {
    /**
     * for names
     * @return - the name of this object
     */
    String get();

    /**
     * renders element.
     */
    void render();

    default Theme getTheme() {
        return new Theme() {
        };
    }
}
