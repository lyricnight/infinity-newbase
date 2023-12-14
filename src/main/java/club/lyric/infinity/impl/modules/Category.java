package club.lyric.infinity.impl.modules;

/**
 * @author vasler
 * categories
 */

public class Category {

    // empty space incase we wanna add anything.

    /**
     * TODO: Add ordinals to sort the categories
     */

    public enum CategoryBase {
        COMBAT("Combat"),
        MISC("Misc"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        PLAYER("Player"),
        EXPLOIT("Exploit"),
        CLIENT("Client");

        public final String label;

        CategoryBase(String label) {
            this.label = label;
        }

        public final String getLabel() {
            return label;
        }
    }
}
