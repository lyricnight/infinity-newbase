package club.lyric.infinity.impl.modules.movement;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.EnumSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.enums.StepMode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static club.lyric.infinity.api.util.client.enums.StepMode.Normal;
import static club.lyric.infinity.api.util.client.enums.StepMode.Vanilla;

public class Step extends ModuleBase {

    public EnumSetting mode =
            new EnumSetting(
                    "Mode",
                    this,
                    Vanilla
            );

    public NumberSetting height =
            new NumberSetting(
                    "Height",
                    this,
                    1.0f,
                    0.0f,
                    2.0f,
                    0.1f
            );

    public Step() {
        super("Step", "steps", Category.Movement);
    }

    @Override
    public void onDisable() {
        mc.player.setStepHeight(0.6f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (nullCheck()) {
            return;
        }
        //todo: fix
        /**
        switch (mode.getMode()) {
            case Vanilla -> {
                mc.player.setStepHeight(height.getFValue());
                break;
            }
            case Normal -> {
                double stepHeight = mc.player.getY() - mc.player.prevY;
                double[] offsets = getOffset(stepHeight);

                if (offsets != null && offsets.length > 1) {
                    for (double offset : offsets) {
                       send(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + offset, mc.player.getZ(), false));
                    }
                }
                break;
            }
        }
        mc.player.setBoundingBox(mc.player.getBoundingBox().offset(0, -1, 0));
         **/
    }

    public double[] getOffset(double height) {
        // enchantment tables
        if (height == 0.75) {
            return new double[] {
                    0.42, 0.753, 0.75
            };
        }
        // end portal frames
        else if (height == 0.8125) {
            return new double[] {
                    0.39, 0.7, 0.8125
            };
        }
        // chests
        else if (height == 0.875) {
            return new double[] {
                    0.39, 0.7, 0.875
            };
        }
        else if (height == 1) {
            return new double[] {
                    0.42, 0.753, 1
            };
        }
        else if (height == 1.5) {
            return new double[] {
                    0.42, 0.75, 1.0, 1.16, 1.23, 1.2
            };
        }
        else if (height == 2) {
            return new double[] {
                    0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43
            };
        }
        else if (height == 2.5) {
            return new double[] {
                    0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907
            };
        }
        return null;
    }
}
