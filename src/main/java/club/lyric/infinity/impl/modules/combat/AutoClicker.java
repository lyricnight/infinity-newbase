package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.Null;
import club.lyric.infinity.api.util.client.math.StopWatch;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends ModuleBase
{

    public NumberSetting max = new NumberSetting("Max", this, 14.0f, 1.0f, 20.0f, 1.0f, " cps");
    public NumberSetting min = new NumberSetting("Min", this, 16.0f, 1.0f, 20.0f, 1.0f, " cps");
    public BooleanSetting blocks = new BooleanSetting("Blocks", true, this);
    private final StopWatch.Single stopWatch = new StopWatch.Single();
    private final StopWatch.Single cpsStopwatch = new StopWatch.Single();
    private int clicks;

    public AutoClicker()
    {
        super("AutoClicker", "aa", Category.Combat);
    }

    @Override
    public String moduleInformation()
    {
        return clicks + " cps";
    }

    @Override
    public void onTickPre()
    {

        if (Null.is()) return;

        if (!blocks.value() && mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) return;

        if (mc.currentScreen != null) return;

        if (cpsStopwatch.hasBeen(1000))
        {
            clicks = 0;
            cpsStopwatch.reset();
        }

        if (min.getIValue() >= max.getIValue())
        {
            if (max.getIValue() == max.getMaximum())
            {
                min.setValue(min.getIValue() - 1);
            }

            max.setValue(min.getFValue() + 1);
        }

        if (!mc.options.attackKey.wasPressed()) return;

        long cps = (long) (1000.0f / (max.getIValue() >= min.getIValue() ? max.getIValue() : ThreadLocalRandom.current().nextInt(min.getIValue(), max.getIValue())));

        if (stopWatch.hasBeen(cps))
        {
            mc.mouse.onMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT, 1, 0);
            mc.mouse.onMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT, 0, 0);
            clicks++;

            stopWatch.reset();
        }

    }
}
