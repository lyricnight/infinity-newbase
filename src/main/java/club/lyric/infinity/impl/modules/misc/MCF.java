package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.impl.events.client.KeyPressEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * @author lyric
 */
//TODO: investigate this not working
public final class MCF extends ModuleBase {
    public MCF() {
        super("MCF", "Add friends using middleclick", Category.Misc);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (mc.currentScreen == null && event.getKey() == 2 && event.getAction() != 0) {
            ChatUtils.sendMessagePrivate("registered click.");
            HitResult player = mc.crosshairTarget;
            if (player instanceof EntityHitResult result) {
                ChatUtils.sendMessagePrivate("registered hitResult.");
                Entity entity = result.getEntity();
                ChatUtils.sendMessagePrivate("Entity: " + entity.getDisplayName());
                if (entity instanceof PlayerEntity playerEntity) {
                    ChatUtils.sendMessagePrivate("registered valid entity, name: " + playerEntity.getDisplayName().getString());
                    Managers.FRIENDS.addFriend(playerEntity.getDisplayName().getString());
                }
            }
        }
    }
}
