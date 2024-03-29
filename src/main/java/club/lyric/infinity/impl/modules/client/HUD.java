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
import club.lyric.infinity.api.util.client.math.apache.ApacheMath;
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

@SuppressWarnings("ConstantConditions")
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

    public BooleanSetting lagOMeter = new BooleanSetting("Lag'O'Meter", true, this);

    public HUD()
    {
        super("HUD", "Displays HUD elements on the screen.", Category.Client);
    }

    private final LinkedList<Long> frames = new LinkedList<>();
    private int chatY = 0;
    private int lagY = 0;
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

        if (packetTimer.hasBeen(1000))
        {
            packets = 0;
            packetTimer.reset();
        }

        int offset = 0;

        Color color = Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(offset) : Managers.MODULES.getModuleFromClass(Colours.class).getColor();

        boolean chatOpened = mc.currentScreen instanceof ChatScreen;

        if (watermark.value())
        {
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

        if (arraylist.value())
        {

            int arrayOffset = 0;

            ArrayList<ModuleBase> moduleList = new ArrayList<>();

            // Gets the module if it is drawn and enabled.
            Managers.MODULES.getModules().forEach(module -> { if (module.isOn() && module.isDrawn()) moduleList.add(module); });

            // Length sorting
            moduleList.sort(Comparator.comparingInt(module -> (int) -Managers.TEXT.width(module.getName(), true)));

            for (ModuleBase module : moduleList) {

                String label = module.getName();

                // X Position
                int x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(label)) - 2;

                Managers.TEXT.drawString(label,
                        x,
                        2 + arrayOffset,
                        hudColor(arrayOffset).getRGB(),
                        true
                );
                arrayOffset += (int) (Managers.TEXT.height(true) + 1);
            }

        }

        if (armorHud.value())
        {
            int width = event.getDrawContext().getScaledWindowWidth();
            int height = event.getDrawContext().getScaledWindowHeight();
            int x = 15;
            for (int i = 3; i >= 0; i--)
            {
                ItemStack stack = mc.player.getInventory().armor.get(i);
                if (!stack.isEmpty())
                {
                    int y;
                    if (mc.player.isInsideWaterOrBubbleColumn() && mc.player.getAir() > 0 && !mc.player.isCreative())
                    {
                        y = 65;
                    }
                    else
                    {
                        y = mc.player.isCreative() ? (mc.player.isRiding() ? 45 : 38) : 55;
                    }
                    final float percent = InventoryUtils.getPercent(stack);
                    event.getDrawContext().getMatrices().push();
                    event.getDrawContext().getMatrices().scale(0.75F, 0.75F, 1F);
                    RenderSystem.disableDepthTest();
                    event.getDrawContext().drawTextWithShadow(mc.textRenderer,
                            Text.of(((int) (percent)) + "%"),
                            (int) (((width >> 1) + x + 1 + getFixedArmorOffset(percent)) * 1.333F),
                            (int) ((height - y - 5) * 1.333F), ColorUtils.toColor(percent / 100.0F * 120.0F,
                                    100.0F,
                                    50.0F,
                                    1.0F).getRGB());
                    RenderSystem.enableDepthTest();
                    event.getDrawContext().getMatrices().scale(1.0F, 1.0F, 1.0F);
                    event.getDrawContext().getMatrices().pop();
                    event.getDrawContext().getMatrices().push();
                    event.getDrawContext().drawItemInSlot(mc.textRenderer, stack, width / 2 + x, height - y);
                    event.getDrawContext().drawItem(stack, width / 2 + x, height - y);
                    event.getDrawContext().getMatrices().pop();
                    x += 18;
                }
            }
        }

        if (potions.value())
        {
            for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                int x = event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(getString(statusEffectInstance))) - 2;
                //
                //VASLER USE MANAGERS.TEXT INSTEAD
                //
                Managers.TEXT.drawString(getString(statusEffectInstance),
                        x,
                        event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                        statusEffectInstance.getEffectType().getColor(),
                        true
                );
                offset += (int) (Managers.TEXT.height(true) + 1);
            }
        }

        // Speed starts
        if (speed.value())
        {
            double distanceX = mc.player.getX() - mc.player.prevX;
            double distanceZ = mc.player.getZ() - mc.player.prevZ;

            String speed = "Speed: " +
                    Formatting.WHITE +
                    MathUtils.roundFloat((MathHelper.sqrt((float) (ApacheMath.pow(distanceX, 2) +
                            ApacheMath.pow(distanceZ, 2))) / 1000) / (0.05F / 3600), 2) +
                    " km/h";

            //
            //VASLER USE MANAGERS.TEXT INSTEAD
            //
            Managers.TEXT.drawString(speed,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(speed)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }
        // Speed ends

        // TPS starts
        if (tps.value())
        {
            String tps = "TPS: " + Formatting.WHITE + Managers.SERVER.getOurTPS();

            //
            //VASLER USE MANAGERS.TEXT INSTEAD
            //
            Managers.TEXT.drawString(tps,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(tps)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }
        // TPS ends

        // Packets Start
        if (packet.value())
        {
            String packetz = "Packets: " + Formatting.GRAY + "[" + Formatting.WHITE + packets + Formatting.GRAY + "]";
            Managers.TEXT.drawString(packetz,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(packetz)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }
        // Packets End

        // TPS starts
        if (ping.value())
        {
            String ping = "Ping: " + Formatting.WHITE + Managers.SERVER.getFastLatencyPing();
            //
            //VASLER USE MANAGERS.TEXT INSTEAD
            //
            Managers.TEXT.drawString(ping,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(ping)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }
        // TPS ends

        // FPS starts
        if (fps.value())
        {
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

            //
            //VASLER USE MANAGERS.TEXT INSTEAD
            //
            Managers.TEXT.drawString(fps,
                    event.getDrawContext().getScaledWindowWidth() - (mc.textRenderer.getWidth(fps)) - 2,
                    event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(event.getDrawContext().getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB(),
                    true
            );
        }
        // FPS ends

        if (lagOMeter.value())
        {
            if (Managers.SERVER.isServerNotResponding()) {
                String lag = "Server hasn't responded in " + String.format("%.1f", (Managers.SERVER.responseTime / 1000f)) + " seconds.";
                Managers.TEXT.drawString(lag,
                        event.getDrawContext().getScaledWindowWidth() / 2.0f - Managers.TEXT.width(lag, true),
                        lagY, color.getRGB(),
                        true
                );
            }
        }

        // Coords Start
        if (coordinates.value())
        {
            boolean inHell = mc.world.getBiome(mc.player.getBlockPos()).equals("Hell");
            if (inHell) {
                Managers.TEXT.drawString("XYZ: " +
                        Formatting.WHITE +
                        iGotzDatDawgInMe(mc.player.getPos().x) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().y) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().z) +
                        Formatting.GRAY +
                        " (" +
                        Formatting.WHITE +
                        iGotzDatDawgInMe(mc.player.getPos().x * 8.0) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().z * 8.0) +
                        Formatting.GRAY +
                        ")",
                        2,
                        event.getDrawContext().getScaledWindowHeight() - 9 - 2 - chatY,
                        color.getRGB(),
                        true
                );
            }
            else {
                Managers.TEXT.drawString("XYZ: " +
                        Formatting.WHITE +
                        iGotzDatDawgInMe(mc.player.getPos().x) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().y) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().z) +
                        Formatting.GRAY +
                        " (" +
                        Formatting.WHITE +
                        iGotzDatDawgInMe(mc.player.getPos().x / 8.0) +
                        ", " +
                        iGotzDatDawgInMe(mc.player.getPos().z / 8.0) +
                        Formatting.GRAY +
                        ")",
                        2,
                        event.getDrawContext().getScaledWindowHeight() - 9 - 2 - chatY,
                        color.getRGB(),
                        true
                );
            }
        }
        // coords end

        // my attempt at a somewhat exponential animation
        if (chatOpened)
        {
            if (chatY == 14) {
                return;
            }
            if (chatY == 9) {
                timer.hasBeen(100);
                chatY++;
                timer.reset();
                return;
            }
            timer.hasBeen(50);
            chatY++;
            timer.reset();
        }
        else
        {
            if (chatY == 0) {
                return;
            }
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

        // lag anim
        if (Managers.SERVER.isServerNotResponding())
        {
            if (lagY == 20) {
                return;
            }
            timer.hasBeen(50);
            lagY++;
            timer.reset();
        }
        else
        {
            if (lagY == 0) {
                return;
            }
            timer.hasBeen(50);
            lagY--;
            timer.reset();
        }
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
    
    public String iGotzDatDawgInMe(double number) {
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
