package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.GameMode;

/**
 * @author vasler
 */
public class Nametags extends ModuleBase {

    public BooleanSetting self = new BooleanSetting("Self", true, this);

    public BooleanSetting entityId = new BooleanSetting("EntityId", true, this);

    public BooleanSetting gamemode = new BooleanSetting("Gamemode", true, this);

    public BooleanSetting latency = new BooleanSetting("Latency", true, this);

    public Nametags() {
        super("Nametags", "Fire", Category.Visual);
    }

    @EventHandler
    public void onRender3D(MatrixStack event) {
        for (Entity entity : mc.world.getEntities())
        {
            if (entity instanceof PlayerEntity player)
            {
                if (player == mc.player && !self.value()) continue;

                double x = player.prevX + (player.getX() - player.prevX) * mc.getTickDelta();
                double y = player.prevY + (player.getY() - player.prevY) * mc.getTickDelta();
                double z = player.prevZ + (player.getZ() - player.prevZ) * mc.getTickDelta();

                float width = Managers.TEXT.width(renderPlayerName(player), true) / 2.0f;

                renderNametag(event, player, width, (float) x, (float) y, (float) z);
            }
        }
        mc.getProfiler().endTick();

        for(Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity player) {
                drawEntityTag(event, player);
            }
        }
    }

    private void renderNametag(MatrixStack matrix, PlayerEntity player, float width, float x, float y, float z) {

        Camera camera = mc.gameRenderer.getCamera();
        matrix.push();

        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));

        matrix.translate(x - camera.getPos().getX(), y + (double) player.getHeight() + (player.isSneaking() ? 0.3f : 0.4f) - camera.getPos().getY(), z - camera.getPos().getZ());

        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        matrix.scale(-0.025F, -0.025F, 1.0F);

        VertexConsumerProvider.Immediate vertexConsumers = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        Managers.TEXT.drawString(renderPlayerName(player), width, 0, -1);

        vertexConsumers.draw();

        RenderSystem.disableBlend();

        matrix.pop();
    }

    public void drawEntityTag(MatrixStack matrix, PlayerEntity entity) {
        Camera camera = mc.gameRenderer.getCamera();
        matrix.push();
        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrix.translate(
                entity.getX() - camera.getPos().x,
                entity.getY() + (double)entity.getHeight() + 0.5 - camera.getPos().y,
                entity.getZ() - camera.getPos().z
        );
        matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrix.scale(-0.025F, -0.025F, 1.0F);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        Managers.TEXT.drawString(renderPlayerName(entity), mc.textRenderer.getWidth(renderPlayerName(entity)) * -0.5F, 0, -1);
        immediate.draw();
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
            //name += getGamemode(getPlayerGamemode(player));
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
    //TODO: add exception for when gamemode = null.
    private String getGamemode(GameMode gamemode) {
        return switch (gamemode) { case SURVIVAL -> " [S] "; case CREATIVE -> " [C] "; case SPECTATOR -> " [I] "; case ADVENTURE -> " [A] ";};
    }
}
