package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;

/**
 * @author lyric
 * TODO.
 */

public final class Aura extends ModuleBase {
    public BooleanSetting require = new BooleanSetting("Require", true, this);
    public NumberSetting range = new NumberSetting("Range", this, 5, 1, 10, 1, "m");
    public NumberSetting wallRange = new NumberSetting("Wall Range", this, 3.5f, 1.0f, 8f, 0.5f, "m");
    public BooleanSetting armor = new BooleanSetting("Armor", false, this);
    public BooleanSetting players = new BooleanSetting("Players", true, this);
    public BooleanSetting animals = new BooleanSetting("Animals", true, this);
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, this);
    public BooleanSetting invis = new BooleanSetting("Invisibles", true, this);


    private Entity target;

    /**
     * convenience
     */
    private boolean rotated;

    public Aura()
    {
        super("Aura", "Melee attack bot.", Category.COMBAT);
    }

    @Override
    public void onUpdate()
    {
    }



}
