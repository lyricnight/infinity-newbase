package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.network.PacketEvent;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author vasler
 * hud
 * @since a while ago
 */

@SuppressWarnings({"ConstantConditions", "unused"})
public final class HUD extends ModuleBase
{
    public BooleanSetting arraylist = new BooleanSetting("Arraylist", true, this);

    public BooleanSetting fps = new BooleanSetting("FPS", true, this);

    public BooleanSetting tps = new BooleanSetting("TPS", true, this);

    public BooleanSetting ping = new BooleanSetting("Ping", true, this);

    public BooleanSetting potions = new BooleanSetting("Potions", true, this);

    public BooleanSetting armorHud = new BooleanSetting("ArmorHUD", true, this);

    public BooleanSetting watermark = new BooleanSetting("Watermark", true, this);
    public BooleanSetting speed = new BooleanSetting("Speed", true, this);

    public BooleanSetting packet = new BooleanSetting("Packets", true, this);

    public BooleanSetting coordinates = new BooleanSetting("Coordinates", true, this);
    public BooleanSetting direction = new BooleanSetting("Direction", true, this);

    public HUD()
    {
        super("HUD", "Displays HUD elements on the screen.", Category.Client);
    }

    private final LinkedList<Long> frames = new LinkedList<>();
    private int chatY = 0;
    private final StopWatch timer = new StopWatch.Single();
    private final StopWatch packetTimer = new StopWatch.Single();
    int packets;

    @EventHandler
    public void onPacketSend(PacketEvent.Send event)
    {
        packets++;
    }

    @Override
    public void onRender2D(Render2DEvent event) {

        if (packetTimer.hasBeen(1000)) {
            packets = 0;
            packetTimer.reset();
        }


        int offset = 0;

        Color color = Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(offset) : Managers.MODULES.getModuleFromClass(Colours.class).getColor();

        boolean chatOpened = mc.currentScreen instanceof ChatScreen;

        if (watermark.value()) {
            Managers.TEXT.drawString(Infinity.CLIENT_NAME +
                            Infinity.VERSION +
                            Formatting.GRAY +
                            " build (" +
                            new SimpleDateFormat("dd/MM/yyyy").format(new Date()) +
                            ")",
                    2,
                    2,
                    hudColor(2).getRGB(),
                    true
            );
        }

        if (arraylist.value()) {

            int arrayOffset = 0;

            ArrayList<ModuleBase> moduleList = new ArrayList<>();

            Managers.MODULES.getModules().forEach(module -> {
                if (module.isOn() && module.isDrawn()) moduleList.add(module);
            });

            moduleList.sort(Comparator.comparingInt(module -> (int) -Managers.TEXT.width(module.getName(), true)));

            for (ModuleBase module : moduleList) {
                String label;

                if (!moduleInformation().isEmpty()) {
                    label = module.getName() + Formatting.GRAY + " [" + Formatting.WHITE + moduleInformation() + Formatting.GRAY + "]";
                } else {
                    label = module.getName();
                }

                float x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(label)) - 2;

                Managers.TEXT.drawString(label,
                        x,
                        2 + arrayOffset,
                        hudColor(arrayOffset).getRGB(),
                        true
                );
                arrayOffset += (int) ((int) (Managers.TEXT.height(true) + 1));
            }

        }

        if (armorHud.value()) {
            int width = event.getDrawContext().getScaledWindowWidth();
            int height = event.getDrawContext().getScaledWindowHeight();
            int x = 15;
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = mc.player.getInventory().armor.get(i);
                if (!stack.isEmpty()) {
                    int y;
                    if (mc.player.isInsideWaterOrBubbleColumn() && mc.player.getAir() > 0 && !mc.player.isCreative()) {
                        y = 65;
                    } else {
                        y = mc.player.isCreative() ? (mc.player.isRiding() ? 45 : 38) : 55;
                    }
                    final float percent = InventoryUtils.getPercent(stack);
                    event.getDrawContext().getMatrices().push();
                    event.getDrawContext().getMatrices().scale(0.75f, 0.75f, 0.75f);
                    RenderSystem.disableDepthTest();
                    event.getDrawContext().drawTextWithShadow(mc.textRenderer,
                            Text.of(((int) (percent)) + "%"),
                            (int) (((width >> 1) + x + 1 + getFixedArmorOffset(percent)) * 1.333f),
                            (int) ((height - y - 5) * 1.333f), ColorUtils.toColor(percent / 100.0f * 120.0f,
                                    100.0f,
                                    50.0f,
                                    1.0f).getRGB());
                    RenderSystem.enableDepthTest();
                    event.getDrawContext().getMatrices().scale(1.0f, 1.0f, 1.0f);
                    event.getDrawContext().getMatrices().pop();
                    event.getDrawContext().getMatrices().push();
                    event.getDrawContext().drawItemInSlot(mc.textRenderer, stack, width / 2 + x, height - y);
                    event.getDrawContext().drawItem(stack, width / 2 + x, height - y);
                    event.getDrawContext().getMatrices().pop();
                    x += 18;
                }
            }
        }

        if (potions.value()) {
            for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                int x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(getString(statusEffectInstance))) - 2;
                Managers.TEXT.drawString(getString(statusEffectInstance),
                        x,
                        event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                        statusEffectInstance.getEffectType().getColor(),
                        true
                );
                offset += (int) (Managers.TEXT.height(true) + 1);
            }
        }

        if (speed.value()) {
            double distanceX = mc.player.getX() - mc.player.prevX;
            double distanceZ = mc.player.getZ() - mc.player.prevZ;

            String speed = "Speed: " +
                    Formatting.WHITE +
                    MathUtils.roundFloat((MathHelper.sqrt((float) (Math.pow(distanceX, 2) +
                            Math.pow(distanceZ, 2))) / 1000) / (0.05F / 3600), 2) +
                    " km/h";

            Managers.TEXT.drawString(speed,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(speed)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (packet.value()) {
            String packet = "Packets: " + Formatting.GRAY + "[" + Formatting.WHITE + packets + Formatting.GRAY + "]";

            Managers.TEXT.drawString(packet,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(packet)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (ping.value() && !mc.isInSingleplayer()) {
            String ping = "Ping: " + Formatting.WHITE + Managers.SERVER.getFastLatencyPing() + "ms";

            Managers.TEXT.drawString(ping,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(ping)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (tps.value() && !mc.isInSingleplayer()) {
            String tps = "TPS: " + Formatting.WHITE + Managers.SERVER.getOurTPS();

            Managers.TEXT.drawString(tps,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(tps)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (fps.value()) {
            long time = System.nanoTime();

            frames.add(time);

            while (true) {

                long f = frames.getFirst();

                final long ONE_SECOND = 1000000L * 1000L;

                if (time - f > ONE_SECOND) frames.remove();

                else break;
            }

            int fpsCount = frames.size();

            String fps = "FPS: " + Formatting.WHITE + fpsCount;

            Managers.TEXT.drawString(fps,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(fps)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
        }

        int coordinateOffset = 0;

        if (coordinates.value()) {
            boolean inHell = mc.world.getRegistryKey().getValue().getPath().equals("nether");
            if (inHell) {
                Managers.TEXT.drawString("XYZ: " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x) +
                                ", " +
                                getFormatting(mc.player.getPos().y) +
                                ", " +
                                getFormatting(mc.player.getPos().z) +
                                Formatting.GRAY +
                                " (" +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x * 8.0) +
                                ", " +
                                getFormatting(mc.player.getPos().z * 8.0) +
                                Formatting.GRAY +
                                ")",
                        2,
                        event.getDrawContext().getScaledWindowHeight() - 9 - 2 - chatY,
                        color.getRGB(),
                        true
                );
            } else {
                Managers.TEXT.drawString("XYZ: " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x) +
                                ", " +
                                getFormatting(mc.player.getPos().y) +
                                ", " +
                                getFormatting(mc.player.getPos().z) +
                                Formatting.GRAY +
                                " (" +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x / 8.0) +
                                ", " +
                                getFormatting(mc.player.getPos().z / 8.0) +
                                Formatting.GRAY +
                                ")",
                        2,
                        event.getDrawContext().getScaledWindowHeight() - 9 - coordinateOffset - 2 - chatY,
                        hudColor(coordinateOffset).getRGB(),
                        true
                );
            }
            coordinateOffset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (direction.value()) {
            String direction = getDirections();
            Managers.TEXT.drawString(direction,
                    2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - coordinateOffset - 2 - chatY,
                    hudColor(coordinateOffset).getRGB(),
                    true
            );
        }

        if (chatOpened) {

            if (chatY == 14) return;

            if (chatY == 9) {
                timer.hasBeen(100);
                chatY++;
                timer.reset();
                return;
            }
            timer.hasBeen(50);
            chatY++;
            timer.reset();
        } else {

            if (chatY == 0) return;

            if (chatY == 5) {
                timer.hasBeen(100);
                chatY--;
                timer.reset();
                return;
            }
            timer.hasBeen(50);
            chatY--;
            timer.reset();
        }

    }
    
    private String getDirections() {
        String[] directions = new String[]{"South ", "South West ", "West ", "North West ", "North ", "North East ", "East ", "South East "};
        String[] axis = new String[]{"+Z", "+Z -X", "-X", "-Z -X", "-Z", "-Z +X", "+X", "+Z +X"};

        String gang = axis[MathUtils.angleDirection(MathHelper.wrapDegrees(mc.player.getYaw()), axis.length)];
        String cool = directions[MathUtils.angleDirection(MathHelper.wrapDegrees(mc.player.getYaw()), directions.length)];

        return cool + Formatting.GRAY + "(" + Formatting.WHITE + getFormatting(MathHelper.wrapDegrees(mc.player.getYaw())) + Formatting.GRAY + ", " + Formatting.WHITE + getFormatting(mc.player.getPitch()) + Formatting.GRAY + ") [" + Formatting.WHITE + gang + Formatting.GRAY + "]";
    }

    public float getFixedArmorOffset(float percent)
    {
        if (percent == 100F) {
            return -0.5F;
        } else if (percent < 10F) {
            return 3.5F;
        } else {
            return 1.5F;
        }
    }
    
    public String getFormatting(double number) {
        return String.format("%.1f", number);
    }


    private String getString(StatusEffectInstance statusEffectInstance)
    {
        int amplifier = statusEffectInstance.getAmplifier();
        Text name = statusEffectInstance.getEffectType().getName();
        return name.getString() +
                (amplifier > 0 ? (" " + (amplifier + 1) + ": ") : ": ") +
                Formatting.WHITE +
                PlayerUtils.getPotionDurationString(statusEffectInstance);
    }

    private Color hudColor(int y) {
        return Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(y) : Managers.MODULES.getModuleFromClass(Colours.class).getColor();
    }

}
