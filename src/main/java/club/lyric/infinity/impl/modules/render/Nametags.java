package club.lyric.infinity.impl.modules.render;

import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

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
            if (entity == mc.player && !self.value()) continue;

        }
        mc.getProfiler().endTick();
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
