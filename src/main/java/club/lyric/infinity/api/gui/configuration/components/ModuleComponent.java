package club.lyric.infinity.api.gui.configuration.components;

import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valser
 */
public class ModuleComponent extends Component {
    private final ModuleBase moduleBase;
    private final List<ModuleBase> moduleComponents = new ArrayList<>();

    public ModuleComponent(ModuleBase moduleBase) {
        this.moduleBase = moduleBase;

        for (Setting setting : moduleBase.getSettings()) {

        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {

    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        final MatrixStack matrix = context.getMatrices();

        matrix.translate(0.0, 0.0, 1.0);

        Render2DUtils.drawRect(context.getMatrices(), x, y, x + width, y + height, new Color(255, 255, 255).getRGB());

        Managers.TEXT.drawString(moduleBase.getName(), x + 2.0f, y + 3.0f, new Color(255, 255, 255).getRGB(), true);

        for (ModuleBase module : moduleComponents) {

            drawScreen(context, mouseX, mouseY, partialTicks);

        }
    }
}
