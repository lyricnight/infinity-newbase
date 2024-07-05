package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.impl.events.mc.TotemPopEvent;
import net.minecraft.item.Items;

public class AutoLog extends ModuleBase
{

    public NumberSetting health = new NumberSetting("Health", this, 16.0f, 1.0f, 36.0f, 0.1f, "hp");
    public BooleanSetting totem = new BooleanSetting("Totem", true, this);
    public NumberSetting totems = new NumberSetting("Totems", this, 1.0f, 1.0f, 43.0f, 1.0f, " totems");

    public AutoLog()
    {
        super("AutoLog", "aa", Category.Combat);
    }

    @Override
    public void onUpdate()
    {
        if (!totem.value())
        {
            if (health.getFValue() >= mc.player.getHealth()) return;

            mc.world.disconnect();
            disable();
        }
    }

    @EventHandler
    public void onTotemPop(TotemPopEvent event)
    {

        if (event.getPlayerEntity() != mc.player) return;

        if (totem.value())
        {
            if (totems.getIValue() >= InventoryUtils.getItemCount(Items.TOTEM_OF_UNDYING)) return;

            mc.world.disconnect();
            disable();
        }
    }
}
