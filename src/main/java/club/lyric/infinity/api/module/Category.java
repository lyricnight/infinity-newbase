package club.lyric.infinity.api.module;

import club.lyric.infinity.api.util.gui.Drag;

/**
 * @author lyric
 * categories
 */

public enum Category {
    CLIENT,
    COMBAT,
    EXPLOIT,
    PLAYER,
    MOVEMENT,
    RENDER;

    // please be GOOD
    private final Drag drag;
    public final int posX;
    public int posY = 20;

    Category() {
        this.posX = 40 + ModuleBase.categoryCount * 120;
        this.drag = new Drag(this.posX, this.posY);
    }

}
