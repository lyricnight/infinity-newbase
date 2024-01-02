package club.lyric.infinity.impl.clickgui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends Screen implements IMinecraft {

    private static GUI INSTANCE;
    private final ArrayList<Window> windows = new ArrayList<>();

    public GUI() {
        super(Text.of("GUI"));
        setInstance();
        load();
    }

    public void setInstance() {
        INSTANCE = this;
    }

    public void load() {
        int x = 5;
        for (final Category category : Category.values()) {
            windows.add(new Window(category, x, 5, true));
            x += 110;
        }
    }

    //20 minutes on bake at 350-400 in glass bowl

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(0, 0, 0, 120).getRGB());
        windows.forEach(windows -> windows.render(context, mouseX, mouseY, delta));
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            mc.setScreen(null);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    static {
        INSTANCE = new GUI();
    }

    public static GUI getInstance() {
        Infinity.LOGGER.info("instance has been gotten");
        if (INSTANCE == null) {
            INSTANCE = new GUI();
        }
        return INSTANCE;
    }
}