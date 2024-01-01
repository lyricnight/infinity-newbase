package club.lyric.infinity.api.util.minecraft.player;

import club.lyric.infinity.api.util.minecraft.IMinecraft;

public record FindItems(int slot, int count) implements IMinecraft {
    public boolean found() {
        return slot != -1;
    }

}