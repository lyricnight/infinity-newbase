package club.lyric.infinity.impl.clickgui.clickgui.impl;


import club.lyric.infinity.api.util.minecraft.IMinecraft;

public abstract class Component implements IMinecraft {
    abstract public void initGui();

    public abstract void keyTyped(char typedChar, int keyCode);

    abstract public void drawScreen(int mouseX, int mouseY);

    abstract public void mouseClicked(int mouseX, int mouseY, int button);

    abstract public void mouseReleased(int mouseX, int mouseY, int state);
}