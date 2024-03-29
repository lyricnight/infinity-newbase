package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

/**
 * @author vasler
 */

@SuppressWarnings("ConstantConditions")
public class Nametags extends ModuleBase {

    public BooleanSetting self = new BooleanSetting("Self", true, this);

    public BooleanSetting entityId = new BooleanSetting("EntityId", true, this);

    public BooleanSetting gamemode = new BooleanSetting("Gamemode", true, this);

    public BooleanSetting latency = new BooleanSetting("Latency", true, this);

    public Nametags() {
        super("Nametags", "Fire", Category.Render);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (mc.gameRenderer == null || mc.getCameraEntity() == null || nullCheck())
        {
            return;
        }
        for (Entity entity : mc.world.getEntities())
        {
            if (entity instanceof PlayerEntity player)
            {
                if (player == mc.player && !self.value()) continue;

                double x = player.prevX + (player.getX() - player.prevX) * mc.getTickDelta();
                double y = player.prevY + (player.getY() - player.prevY) * mc.getTickDelta();
                double z = player.prevZ + (player.getZ() - player.prevZ) * mc.getTickDelta();

                float width = Managers.TEXT.width(renderPlayerName(player), true) / 2.0f;

                renderNametag(event.getMatrix(), player, width, (float) x, (float) y, (float) z, 2.0f);
            }
        }
        mc.getProfiler().endTick();
    }

    private void renderNametag(MatrixStack matrix, PlayerEntity player, float width, float x, float y, float z, float scale) {
        Camera camera = new Camera();
        Vec3d pos = camera.getPos();

        matrix.push();
        matrix.translate(x - pos.getX(), y + (double) player.getHeight() + (player.isSneaking() ? 0.3f : 0.4f) - pos.getY(), z - pos.getZ());

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        matrix.scale(-scale, -scale, -1.0f);

        Managers.TEXT.drawString(renderPlayerName(player), width, 0, -1, true);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        matrix.pop();
    }

    private String renderPlayerName(PlayerEntity player) {
        String name = player.getName().getString();

        if (entityId.value())
        {
            name += " ID: " + player.getId();
        }

        if (gamemode.value())
        {
            name += getGamemode(getPlayerGamemode(player));
        }

        if (latency.value())
        {
            name += " " + getPlayerLatency(player) + "ms ";
        }

        return name;
    }

    public static GameMode getPlayerGamemode(PlayerEntity player) {
        if (player == null) return null;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? null : playerListEntry.getGameMode();
    }

    public static int getPlayerLatency(PlayerEntity player) {
        if (player == null) return 0;
        if (mc.getNetworkHandler() == null) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? 0 : playerListEntry.getLatency();
    }

    private String getGamemode(GameMode gamemode) {
        return switch (gamemode) { case SURVIVAL -> " [S] "; case CREATIVE -> " [C] "; case SPECTATOR -> " [I] "; case ADVENTURE -> " [A] "; };
    }
}
