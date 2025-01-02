package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.gui.Menu;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ColorSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.render.colors.JColor;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;


/**
 * @author lyric
 */
public final class ClickGUI extends ModuleBase {

    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(180, 50, 255)), false);

    public NumberSetting alpha = new NumberSetting("BackgroundAlpha", this, 60, 0, 255, 5);

    public BooleanSetting resizing = new BooleanSetting("Resizing", false, this);

    public ClickGUI() {
        super("ClickGui", "Displays a graphical user interface.", Category.CLIENT);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        if (mc.world == null) {
            disable();
            return;
        }
        Menu.getInstance().toggle();
        mc.mouse.unlockCursor();
    }

    @Override
    public void onDisable() {
        if (mc.world == null) {
            return;
        }
        mc.setScreenAndRender(null);
        Menu.getInstance().toggle();
        if (mc.currentScreen == null) mc.mouse.lockCursor();
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS && (event.getKey() == GLFW.GLFW_KEY_ESCAPE || event.getKey() == bind.getCode())) {
            setEnabled(false);
        }
    }

    @Override
    public void onRender2D(DrawContext context) {
        Render2DUtils.drawRect(context.getMatrices(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), alpha.getIValue()).getRGB());
    }
}
