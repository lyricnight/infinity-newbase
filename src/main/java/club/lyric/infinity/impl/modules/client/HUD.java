package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.manager.Managers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
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

@SuppressWarnings({"unused", "ConstantConditions"})
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
    public BooleanSetting greeting = new BooleanSetting("Greeting", true, this);
    public BooleanSetting shadow = new BooleanSetting("Shadow", true, this);
    public ModeSetting casing = new ModeSetting("Casing", this, "Normal", "Normal", "Lowercase", "Uppercase", "Random");
    public ModeSetting sorting = new ModeSetting("Sorting", this, "Length", "Length", "Alphabetical");
    public ModeSetting effectHud = new ModeSetting("EffectHud", this, "Move", "Remove", "Keep", "Move");
    public ModeSetting potionColors = new ModeSetting("PotionColors", this, "Normal", "Normal", "Global");

    public HUD()
    {
        super("HUD", "Displays HUD elements on the screen.", Category.Client);
    }

    private final LinkedList<Long> frames = new LinkedList<>();
    private int chatY = 0;
    private int effectY = 0;
    private final StopWatch timer = new StopWatch.Single();
    private final StopWatch packetTimer = new StopWatch.Single();
    int packets;

    @EventHandler
    public void onPacketSend(PacketEvent.Send event)
    {
        packets++;
    }

    @Override
    public void onRender2D(DrawContext context) {

        if (packetTimer.hasBeen(1000)) {
            packets = 0;
            packetTimer.reset();
        }


        int offset = 0;

        Color color = Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(offset) : Managers.MODULES.getModuleFromClass(Colours.class).getColor();

        boolean chatOpened = mc.currentScreen instanceof ChatScreen;

        if (watermark.value()) {
            Managers.TEXT.drawString(getLabel(Infinity.CLIENT_NAME +
                            Infinity.VERSION +
                            Formatting.GRAY +
                            " build (" +
                            new SimpleDateFormat("dd/MM/yyyy").format(new Date()) +
                            ")"),
                    2,
                    2,
                    hudColor(2).getRGB()
            );
        }

        if (greeting.value())
        {

            String text = "Hello, " + mc.player.getName().getString() + " :^)";

            int x = (int) (context.getScaledWindowWidth() / 2 - Managers.TEXT.width(text, true) / 2 + 2);

            Managers.TEXT.drawString(getLabel(text),
                    x,
                    2,
                    hudColor(2).getRGB()
            );

        }

        if (arraylist.value()) {

            int arrayOffset = 0;

            ArrayList<ModuleBase> moduleList = new ArrayList<>();

            Managers.MODULES.getModules().forEach(module -> {
                if (module.isOn() && module.isDrawn()) moduleList.add(module);
            });

            if (sorting.is("Alphabetical"))
            {
                moduleList.sort(Comparator.comparing(module -> getLabel(module.getName() + module.getSuffix())));
            }
            else
            {
                moduleList.sort(Comparator.comparingInt(module -> (int) -Managers.TEXT.width(getLabel(module.getName() + module.getSuffix()), true)));
            }

            for (ModuleBase module : moduleList) {

                String label = module.getName() + module.getSuffix();

                if (effectHud.is("Move"))
                {
                    for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects())
                    {
                        effectY = 27;
                        if (!statusEffectInstance.getEffectType().isBeneficial())
                        {
                            effectY = 53;
                            break;
                        }
                    }
                    if (mc.player.getStatusEffects().isEmpty())
                    {
                        effectY = 0;
                    }
                }
                else
                {
                    effectY = 0;
                }

                float x = context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(label), true)) - 2;

                Managers.TEXT.drawString(getLabel(label),
                        x,
                        2 + arrayOffset + effectY,
                        hudColor(arrayOffset).getRGB()
                );
                arrayOffset += (int) ((int) (Managers.TEXT.height(true) + 1));
            }

        }

        if (armorHud.value()) {
            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();
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
                    context.getMatrices().push();
                    context.getMatrices().scale(0.75f, 0.75f, 0.75f);
                    RenderSystem.disableDepthTest();
                    context.drawTextWithShadow(mc.textRenderer,
                            Text.of(((int) (percent)) + "%"),
                            (int) (((width >> 1) + x + 1 + getFixedArmorOffset(percent)) * 1.333f),
                            (int) ((height - y - 5) * 1.333f), ColorUtils.toColor(percent / 100.0f * 120.0f,
                                    100.0f,
                                    50.0f,
                                    1.0f).getRGB());
                    RenderSystem.enableDepthTest();
                    context.getMatrices().scale(1.0f, 1.0f, 1.0f);
                    context.getMatrices().pop();
                    context.getMatrices().push();
                    context.drawItemInSlot(mc.textRenderer, stack, width / 2 + x, height - y);
                    context.drawItem(stack, width / 2 + x, height - y);
                    context.getMatrices().pop();
                    x += 18;
                }
            }
        }

        if (potions.value()) {
            for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                int x = (int) (context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(getString(statusEffectInstance)), true)) - 2);
                Managers.TEXT.drawString(getLabel(getString(statusEffectInstance)),
                        x,
                        context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                        potionColors.is("Normal") ? statusEffectInstance.getEffectType().getColor() : hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
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

            Managers.TEXT.drawString(getLabel(speed),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(speed), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (packet.value()) {
            String packet = "Packets: " + Formatting.GRAY + "[" + Formatting.WHITE + packets + Formatting.GRAY + "]";

            Managers.TEXT.drawString(getLabel(packet),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(packet), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
            );
            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (ping.value() && !mc.isInSingleplayer()) {

            String pingString;

            if (Managers.SERVER.getFastLatencyPing() != 0)
            {
                pingString = "Latency: " + Formatting.WHITE + Managers.SERVER.getServerPing() + " [" + Managers.SERVER.getFastLatencyPing() + "]";
            }
            else
            {
                pingString = "Latency: " + Formatting.WHITE + Managers.SERVER.getServerPing();
            }

            Managers.TEXT.drawString(getLabel(pingString),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(pingString), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
            );

            offset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (tps.value() && !mc.isInSingleplayer()) {
            String tps = "TPS: " + Formatting.WHITE + Managers.SERVER.getOurTPS();

            Managers.TEXT.drawString(getLabel(tps),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(tps), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
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

            Managers.TEXT.drawString(getLabel(fps),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(fps), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - chatY,
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2 - chatY).getRGB()
            );
        }

        int coordinateOffset = 0;

        if (coordinates.value()) {
            boolean inHell = mc.world.getRegistryKey().getValue().getPath().equals("nether");
            if (inHell) {
                Managers.TEXT.drawString(getLabel("XYZ: " +
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
                                ")"),
                        2,
                        context.getScaledWindowHeight() - 9 - 2 - chatY,
                        color.getRGB()
                );
            } else {
                Managers.TEXT.drawString(getLabel("XYZ: " +
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
                                ")"),
                        2,
                        context.getScaledWindowHeight() - 9 - coordinateOffset - 2 - chatY,
                        hudColor(coordinateOffset).getRGB()
                );
            }
            coordinateOffset += (int) (Managers.TEXT.height(true) + 1);
        }

        if (direction.value()) {
            String direction = getDirections();
            Managers.TEXT.drawString(getLabel(direction),
                    2,
                    context.getScaledWindowHeight() - 9 - coordinateOffset - 2 - chatY,
                    hudColor(coordinateOffset).getRGB()
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

    public String getLabel(String label)
    {
        if (casing.is("Lowercase"))
        {
            return label.toLowerCase();
        }
        else if (casing.is("Uppercase"))
        {
            return label.toUpperCase();
        }
        else if (casing.is("Random"))
        {
            // skided
            char[] array = label.toCharArray();
            int chars = 0;

            while (chars < label.length())
            {

                final char character;

                if (Character.isUpperCase(array[chars]))
                {
                    character = Character.toLowerCase(array[chars]);
                }
                else
                {
                    character = Character.toUpperCase(array[chars]);
                }

                array[chars] = character;

                chars += 2;
            }
            return String.valueOf(array);

        }
        return label;
    }

}
