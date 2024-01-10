package club.lyric.infinity.api.module;

import club.lyric.infinity.api.util.client.gui.Drag;

/**
 * @author valser
 * categories
 */

public enum Category {
    CLIENT,
    COMBAT,
    EXPLOIT,
    PLAYER,
    MOVEMENT,
    RENDER;
    public final int posX;
    public final int posY = 20;

    Category() {
        this.posX = 40 + ModuleBase.categoryCount * 120;
        Drag drag = new Drag(this.posX, this.posY);
    }

}
