package club.lyric.infinity.api.gui.configuration.components;

import club.lyric.infinity.api.gui.interfaces.Component;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

/**
 * @author valser
 */
public class BaseComponent extends Component {

    public BaseComponent(Setting setting, float x, float y, float width, float height) {
        this.setting = setting;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        Render2DUtils.drawRect(context.getMatrices(), x, y, x + width, y + height, new Color(255, 255, 255).getRGB());

        Managers.TEXT.drawString(setting.getName(), x + 2.0f, y + 3.0f, new Color(255, 255, 255).getRGB(), true);

    }
}
