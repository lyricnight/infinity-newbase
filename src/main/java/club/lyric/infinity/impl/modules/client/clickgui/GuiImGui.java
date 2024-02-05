package club.lyric.infinity.impl.modules.client.clickgui;

import club.lyric.infinity.api.util.client.gui.InfinityGUI;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GuiImGui extends Screen implements IMinecraft {

    private static GuiImGui clickGui;
    private Screen parent;

    protected GuiImGui(Text title) {
        super(title);
    }

    public Screen getParent() {
        return parent;
    }

    public static GuiImGui getClickGui() {
        return clickGui == null ? (clickGui = new GuiImGui(Text.of("Infinity"))) : clickGui;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (parent != null) parent.render(context, mouseX, mouseY, delta);

        InfinityGUI.getInstance().toggle();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        super.mouseClicked(mouseX, mouseY, clickedButton);
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        super.mouseReleased(mouseX, mouseY, releaseButton);
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}