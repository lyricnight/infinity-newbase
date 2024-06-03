package club.lyric.infinity.impl.modules.visual;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.awt.*;

/**
 * @author vasler
 */
@SuppressWarnings("ConstantConditions")
public class Nametags extends ModuleBase {

    public BooleanSetting self = new BooleanSetting("Self", true, this);

    public BooleanSetting entityId = new BooleanSetting("EntityId", true, this);

    public BooleanSetting gamemode = new BooleanSetting("GameMode", true, this);

    public BooleanSetting latency = new BooleanSetting("Latency", true, this);
    public BooleanSetting health = new BooleanSetting("Health", true, this);
    public BooleanSetting totemPops = new BooleanSetting("TotemPops", true, this);

    public Nametags() {
        super("Nametags", "Fire", Category.Visual);
    }

    @Override
    public void onRender3D(MatrixStack event) {
        for (Entity entity : mc.world.getEntities())
        {
            Vec3d interpolate = Interpolation.getRenderPosition(mc.getCameraEntity(), mc.getTickDelta());
            if (entity instanceof PlayerEntity player)
            {

                if (!self.value() && player == mc.player) continue;

                renderNameTag(player, event, interpolate);
            }

        }
    }

    private void renderNameTag(PlayerEntity entity, MatrixStack matrices, Vec3d inter)
    {
        Vec3d interpolate = Interpolation.getRenderPosition(entity, mc.getTickDelta());
        Vec3d pos = getCameraPos();

        double x = entity.getX() - interpolate.getX();
        double y = entity.getY() - interpolate.getY();
        double z = entity.getZ() - interpolate.getZ();

        TextRenderer textRenderer = mc.textRenderer;
        Render3DUtils.enable3D();

        double distX = (mc.player.getX() - inter.getX()) - x;
        double distY = (mc.player.getY() - inter.getY()) - y;
        double distZ = (mc.player.getZ() - inter.getZ()) - z;

        double dist = MathHelper.sqrt((float) (distX * distX + distY * distY + distZ * distZ));

        if (dist > 4096.0) return;

        float scale = 0.01f * (float) dist;

        if (dist <= 8) {
            scale = 0.025f;
        }

        matrices.push();
        matrices.translate(
                x - pos.getX(),
                y + (double) entity.getHeight() + (entity.isSneaking() ? 0.5f : 0.53f) - pos.getY(),
                z - pos.getZ()
        );
        matrices.multiply(mc.getEntityRenderDispatcher().camera.getRotation());
        matrices.scale(-scale, -scale, scale);

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        //DrawContext context = new DrawContext(mc, immediate);

        Render2DUtils.drawRect(matrices, (float) -mc.textRenderer.getWidth(renderPlayerName(entity)) / 2 - 2, 0, mc.textRenderer.getWidth(renderPlayerName(entity)) + 1, mc.textRenderer.fontHeight + 2.0f, new Color(0, 4, 0, 85).getRGB());
        Render2DUtils.drawOutlineRect(matrices, (float) -mc.textRenderer.getWidth(renderPlayerName(entity)) / 2 - 2, 0, mc.textRenderer.getWidth(renderPlayerName(entity)) + 1, mc.textRenderer.fontHeight + 2.0f, new Color(0, 0, 0, 51).getRGB());
        textRenderer.draw(renderPlayerName(entity), (float) -mc.textRenderer.getWidth(renderPlayerName(entity)) / 2 + 1, 2, getNameColor(entity), false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        //context.drawTextWithShadow(textRenderer, renderPlayerName(entity), (int) ((float) -mc.textRenderer.getWidth(renderPlayerName(entity)) / 2 + 1), 2, getNameColor(entity));
        //Managers.TEXT.drawString(renderPlayerName(entity), (float) mc.textRenderer.getWidth(renderPlayerName(entity)) / 2, 0, -1);

        immediate.draw();
        matrices.pop();
        Render3DUtils.disable3D();
    }

    private String renderPlayerName(PlayerEntity player) {
        String name = player.getName().getString();

        if (entityId.value())
        {
            name += " ID: " + player.getId();
        }

        if (gamemode.value() && mc.getNetworkHandler() != null) {
            PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
            if (playerListEntry != null) {
                name += getGamemode(getPlayerGamemode(player));
            }
        }

        if (latency.value())
        {
            name += " " + getPlayerLatency(player) + "ms ";
        }

        if (health.value()) {
            double health = Math.ceil(player.getHealth() + player.getAbsorptionAmount());

            Formatting color;
            if (health > 18) {
                color = Formatting.GREEN;
            } else if (health > 16) {
                color = Formatting.DARK_GREEN;
            } else if (health > 12) {
                color = Formatting.YELLOW;
            } else if (health > 8) {
                color = Formatting.GOLD;
            } else if (health > 4) {
                color = Formatting.RED;
            } else {
                color = Formatting.DARK_RED;
            }
            name += color + "" + (int) health + " ";
        }

        if (totemPops.value()) {
            double pops = Managers.OTHER.totemPopMap.get(player.getId());

            Formatting color = getFormatting(pops);
            name += color + "" + pops + " ";
        }

        return name;
    }

    private static Formatting getFormatting(double pops) {
        Formatting color = null;
        if (pops >= 1) {
            color = Formatting.GREEN;
        }
        if (pops >= 2) {
            color = Formatting.DARK_GREEN;
        }
        if (pops >= 3) {
            color = Formatting.YELLOW;
        }
        if (pops >= 4) {
            color = Formatting.GOLD;
        }
        if (pops >= 5) {
            color = Formatting.RED;
        }
        if (pops >= 6) {
            color = Formatting.DARK_RED;
        }
        return color;
    }

    protected int getNameColor(PlayerEntity player) {
        if (Managers.FRIENDS.isFriend(player)) {
            return 0xFF00FF;
        }

        if (player.isSneaking()) {
            return 0xFF9900;
        }

        return -1;
    }
    public static GameMode getPlayerGamemode(PlayerEntity player) {
        if (player == null) return null;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? null : playerListEntry.getGameMode();
    }

    public static Vec3d getCameraPos()
    {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null)
        {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }

    public static int getPlayerLatency(PlayerEntity player)
    {
        if (player == null) return 0;

        if (mc.getNetworkHandler() == null) return 0;

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? 0 : playerListEntry.getLatency();
    }


    //add exception for when gamemode = null.
    private String getGamemode(GameMode gamemode) {
        return switch (gamemode) { case SURVIVAL -> " [S]"; case CREATIVE -> " [C]"; case SPECTATOR -> " [I]"; case ADVENTURE -> " [A]";};
    }
}
