package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.api.util.minecraft.IMinecraft;

/**
 * @author vasler
 * @since 12/17/2023
 */
public interface Screen extends IMinecraft {

    void initGui();

    void keyTyped(char typedChar, int keyCode);

    void drawScreen(int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int button);

    void mouseReleased(int mouseX, int mouseY, int state);

}