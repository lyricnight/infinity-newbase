package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * @author lyric
 */
public final class MCF extends ModuleBase {
    public MCF() {
        super("MCF", "Add friends using middleclick", Category.MISC);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (mc.currentScreen == null && event.getKey() == 2) {
            HitResult player = mc.crosshairTarget;
            if (player instanceof EntityHitResult result && result.getEntity() instanceof PlayerEntity) {
                Managers.FRIENDS.addFriend(result.getEntity().getDisplayName().getString());
            }
        }
    }
}
