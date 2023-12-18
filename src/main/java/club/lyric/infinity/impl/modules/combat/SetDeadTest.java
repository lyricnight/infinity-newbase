package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;

public class SetDeadTest extends ModuleBase {

    public SetDeadTest() {
        super("torll", "", Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        //Entity crystal = new EndCrystalEntity();
        //crystal.remove(Entity.RemovalReason.KILLED);
    }
}
