package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.Version;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.client.render.anim.Animation;
import club.lyric.infinity.api.util.client.render.anim.Easing;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.minecraft.player.InventoryUtils;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.impl.events.network.PacketEvent;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * @author vasler
 * hud
 * @since a while ago
 */

@SuppressWarnings({"unused"})
public final class HUD extends ModuleBase {
    public BooleanSetting arraylist = new BooleanSetting("Arraylist", true, this);
    public BooleanSetting fps = new BooleanSetting("FPS", true, this);
    public BooleanSetting tps = new BooleanSetting("TPS", true, this);
    public BooleanSetting ping = new BooleanSetting("Ping", true, this);
    public BooleanSetting potions = new BooleanSetting("Potions", true, this);
    public BooleanSetting armorHud = new BooleanSetting("Armor", true, this);
    public BooleanSetting percentage = new BooleanSetting("Percentage", true, this);
    public BooleanSetting watermark = new BooleanSetting("Watermark", true, this);
    public BooleanSetting build = new BooleanSetting("Build", true, this);
    public BooleanSetting speed = new BooleanSetting("Speed", true, this);
    public BooleanSetting packet = new BooleanSetting("Packets", true, this);
    public BooleanSetting serverBrand = new BooleanSetting("ServerBrand", true, this);
    public BooleanSetting spotify = new BooleanSetting("Spotify", true, this);
    public BooleanSetting durability = new BooleanSetting("Durability", true, this);
    public BooleanSetting coordinates = new BooleanSetting("Coordinates", true, this);
    public BooleanSetting direction = new BooleanSetting("Direction", true, this);
    public BooleanSetting greeting = new BooleanSetting("Greeting", true, this);
    public BooleanSetting shadow = new BooleanSetting("Shadow", true, this);
    public ModeSetting casing = new ModeSetting("Casing", this, "Normal", "Normal", "Lowercase", "Uppercase", "Random");
    public ModeSetting sorting = new ModeSetting("Sorting", this, "Length", "Length", "Alphabetical");
    public ModeSetting effectHud = new ModeSetting("EffectHud", this, "Move", "Remove", "Keep", "Move");
    public ModeSetting potionColors = new ModeSetting("PotionColors", this, "Normal", "Normal", "Global");

    public HUD() {
        super("HUD", "Displays HUD elements on the screen.", Category.CLIENT);
    }

    private final LinkedList<Long> frames = new LinkedList<>();
    private int effectY = 0;
    private final StopWatch packetTimer = new StopWatch.Single();
    private final StopWatch spotifyTimer = new StopWatch.Single();
    int packets;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUAD, 150);

    // Info
    private final Animation fpsWidth = new Animation(Easing.EASE_OUT_QUAD, 150);
    private final Animation packetWidth = new Animation(Easing.EASE_OUT_QUAD, 150);
    private final Animation speedWidth = new Animation(Easing.EASE_OUT_QUAD, 150);
    private final Animation tpsWidth = new Animation(Easing.EASE_OUT_QUAD, 150);
    private final Animation pingWidth = new Animation(Easing.EASE_OUT_QUAD, 150);
    private final Animation duraWidth = new Animation(Easing.EASE_OUT_QUAD, 150);

    @EventHandler(priority = 1)
    public void onPacketSend(PacketEvent.Send event) {
        packets++;
    }

    @Override
    public void onRender2D(DrawContext context) {

        if (packetTimer.hasBeen(1000)) {
            packets = 0;
            packetTimer.reset();
        }


        int offset = 0;

        boolean chatOpened = mc.currentScreen instanceof ChatScreen;

        if (mc.getDebugHud().shouldShowDebugHud() || Managers.MODULES.getModuleFromClass(ClickGUI.class).isOn()) return;

        if (watermark.value())
        {
            String watermark = Infinity.CLIENT_NAME + Infinity.VERSION;

            if (build.value())
            {
                watermark += Formatting.GRAY + " build " + Version.DATE;
            }

            Managers.TEXT.drawString(getLabel(watermark),
                    2,
                    2,
                    hudColor(2).getRGB()
            );
        }

        if (greeting.value()) {

            String text = "Hello, " + mc.player.getName().getString() + " :^)";

            LocalDate currentDate = LocalDate.now();

            boolean christmas = currentDate.getMonthValue() == 12 && currentDate.getDayOfMonth() == 25;

            if (christmas) {
                text = "Merry Christmas, " + mc.player.getName().getString() + " :^)";
            }

            int x = (int) (context.getScaledWindowWidth() / 2.0f - Managers.TEXT.width(text, true) / 2 + 2);

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

            if (sorting.is("Alphabetical")) {
                moduleList.sort(Comparator.comparing(module -> getLabel(module.getName() + module.getSuffix())));
            } else {
                moduleList.sort(Comparator.comparingInt(module -> (int) -Managers.TEXT.width(getLabel(module.getName() + module.getSuffix()), true)));
            }

            for (ModuleBase module : moduleList) {

                String text = module.getName() + module.getSuffix();

                long moduleCount = moduleList.size();

                if (effectHud.is("Move")) {
                    for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                        effectY = 27;
                        if (!statusEffectInstance.getEffectType().value().isBeneficial()) {
                            effectY = 53;
                            break;
                        }
                    }
                    if (mc.player.getStatusEffects().isEmpty()) {
                        effectY = 0;
                    }
                } else {
                    effectY = 0;
                }

                float x = context.getScaledWindowWidth() - Managers.TEXT.width(text, true) - 2;

                Managers.TEXT.drawString(getLabel(text),
                        x,
                        2 + arrayOffset + effectY,
                        hudColor(arrayOffset).getRGB()
                );

                arrayOffset += Managers.TEXT.height(true) + 1;
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
                    if (mc.player.isSubmergedInWater() && !mc.player.isCreative()) {
                        y = 65;
                    } else {
                        y = mc.player.isCreative() ? (mc.player.isRiding() ? 45 : 38) : 55;
                    }
                    float percent = InventoryUtils.getPercent(stack);

                    context.getMatrices().push();
                    context.getMatrices().scale(0.625f, 0.625f, 0.625f);
                    Managers.TEXT.drawString(
                            ((int) (percent * 100.0f)) + "%",
                            (x + 2) * 1.6f, (height - y - 5) * 1.6f - 1,
                            ColorUtils.toColor(percent / 100.0f * 120.0f, 100.0f, 50.0f,
                                    1.0f).getRGB());
                    context.getMatrices().scale(1.0f, 1.0f, 1.0f);
                    context.getMatrices().pop();

                    context.drawItem(stack, width / 2 + x, height - y);
                    context.drawItemTooltip(mc.textRenderer, stack, width / 2 + x, height - y);
                    x += 18;
                }
            }
        }

        if (potions.value()) {
            ArrayList<StatusEffectInstance> effectList = new ArrayList<>(mc.player.getStatusEffects());

            effectList.sort(Comparator.comparing(
                    effect -> I18n.translate(effect.getEffectType().value().getTranslationKey()), Comparator.reverseOrder()
            ));

            for (StatusEffectInstance statusEffectInstance : effectList) {
                int x = (int) (context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(getString(statusEffectInstance)), true)) - 2);

                Managers.TEXT.drawString(getLabel(getString(statusEffectInstance)),
                        x,
                        context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                        potionColors.is("Normal") ? statusEffectInstance.getEffectType().value().getColor() : hudColor((int) (context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue())).getRGB()
                );
                offset += Managers.TEXT.height(true) + 1;
            }
        }

        if (spotify.value() && Managers.SPOTIFY.isSpotifyRunning()) {
            Managers.TEXT.drawString(getLabel(Managers.SPOTIFY.getMedia()),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(Managers.SPOTIFY.getMedia()), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );

            offset += Managers.TEXT.height(true) + 1;
        }

        if (serverBrand.value()) {

            String server = "ServerBrand " + Formatting.WHITE + (mc.isInSingleplayer() ? "Singleplayer (Integrated)" : mc.getNetworkHandler().getBrand());

            Managers.TEXT.drawString(getLabel(server),
                    context.getScaledWindowWidth() - Managers.TEXT.width(getLabel(server), true) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );

            offset += Managers.TEXT.height(true) + 1;
        }

        if (durability.value() && mc.player.getMainHandStack().isDamageable()) {

            int max = mc.player.getMainHandStack().getMaxDamage();
            int dmg = mc.player.getMainHandStack().getDamage();

            String damage = String.valueOf(max - dmg);


            String dura = "Durability";

            duraWidth.run(Managers.TEXT.width(getLabel(dura), true));

            Managers.TEXT.drawString(getLabel(dura),
                    context.getScaledWindowWidth() - duraWidth.getValue() - 4 - (Managers.TEXT.width(getLabel(damage), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );

            Managers.TEXT.drawString(getLabel(damage),
                    context.getScaledWindowWidth() - (Managers.TEXT.width(getLabel(damage), true)) - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    ColorUtils.toColor((float) (max - dmg) / max * 120.0f,
                            100.0f,
                            50.0f,
                            1.0f).getRGB()
            );

            offset += Managers.TEXT.height(true) + 1;
        }

        if (speed.value()) {
            double distanceX = mc.player.getX() - mc.player.prevX;
            double distanceZ = mc.player.getZ() - mc.player.prevZ;

            String speed = "Speed " +
                    Formatting.WHITE +
                    // changed formatting cause it fucks with width schanging
                    String.format("%.2f", (MathHelper.sqrt((float) (Math.pow(distanceX, 2) +
                            Math.pow(distanceZ, 2))) / 1000) / (0.05F / 3600)) +
                    " km/h";

            speedWidth.run((Managers.TEXT.width(getLabel(speed), true)));

            Managers.TEXT.drawString(getLabel(speed),
                    context.getScaledWindowWidth() - speedWidth.getValue() - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );
            offset += Managers.TEXT.height(true) + 1;
            speedWidth.reset();
        }

        if (packet.value()) {
            String packet = "Packets " + Formatting.GRAY + "[" + Formatting.WHITE + packets + Formatting.GRAY + "]";

            packetWidth.run((Managers.TEXT.width(getLabel(packet), true)));

            Managers.TEXT.drawString(getLabel(packet),
                    context.getScaledWindowWidth() - packetWidth.getValue() - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );
            offset += Managers.TEXT.height(true) + 1;
        }

        if (ping.value() && !mc.isInSingleplayer()) {

            String pingString;

            if (Managers.SERVER.getFastLatencyPing() != 0) {
                pingString = "Ping " + Formatting.WHITE + Managers.SERVER.getServerPing() + "ms" + " [" + Managers.SERVER.getFastLatencyPing() + "]";
            } else {
                pingString = "Ping " + Formatting.WHITE + Managers.SERVER.getServerPing() + "ms";
            }

            pingWidth.run((Managers.TEXT.width(getLabel(pingString), true)));

            Managers.TEXT.drawString(getLabel(pingString),
                    context.getScaledWindowWidth() - pingWidth.getValue() - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );

            offset += Managers.TEXT.height(true) + 1;
        }

        if (tps.value() && !mc.isInSingleplayer()) {
            String tps = "TPS " + Formatting.WHITE + Managers.SERVER.getOurTPS();

            tpsWidth.run((Managers.TEXT.width(getLabel(tps), true)));

            Managers.TEXT.drawString(getLabel(tps),
                    context.getScaledWindowWidth() - tpsWidth.getValue() - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );
            offset += Managers.TEXT.height(true) + 1;
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

            String fps = "FPS " + Formatting.WHITE + fpsCount;

            fpsWidth.run(Managers.TEXT.width(getLabel(fps), true));

            Managers.TEXT.drawString(getLabel(fps),
                    context.getScaledWindowWidth() - fpsWidth.getValue() - 2,
                    context.getScaledWindowHeight() - 9 - offset - 2 - animation.getValue(),
                    hudColor(context.getScaledWindowHeight() - 9 - offset - 2).getRGB()
            );
        }

        int coordinateOffset = 0;

        if (coordinates.value()) {
            boolean inHell = mc.world.getRegistryKey().getValue().getPath().equals("nether");
            if (inHell) {
                Managers.TEXT.drawString(getLabel("XYZ " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x) +
                                ", " +
                                getFormatting(mc.player.getPos().y) +
                                ", " +
                                getFormatting(mc.player.getPos().z) +
                                Formatting.GRAY +
                                " [" +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x * 8.0) +
                                ", " +
                                getFormatting(mc.player.getPos().z * 8.0) +
                                Formatting.GRAY +
                                "] " +
                                Formatting.GRAY +
                                "[" +
                                Formatting.WHITE +
                                getFormatting(MathHelper.wrapDegrees(mc.player.getYaw())) +
                                Formatting.GRAY +
                                ", " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPitch()) +
                                Formatting.GRAY +
                                "]"
                        ),
                        2,
                        context.getScaledWindowHeight() - 9 - 2 - animation.getValue(),
                        hudColor(coordinateOffset).getRGB()
                );
            } else {
                Managers.TEXT.drawString(getLabel("XYZ " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x) +
                                ", " +
                                getFormatting(mc.player.getPos().y) +
                                ", " +
                                getFormatting(mc.player.getPos().z) +
                                Formatting.GRAY +
                                " [" +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPos().x / 8.0) +
                                ", " +
                                getFormatting(mc.player.getPos().z / 8.0) +
                                Formatting.GRAY +
                                "] " +
                                Formatting.GRAY +
                                "(" +
                                Formatting.WHITE +
                                getFormatting(MathHelper.wrapDegrees(mc.player.getYaw())) +
                                Formatting.GRAY +
                                ", " +
                                Formatting.WHITE +
                                getFormatting(mc.player.getPitch()) +
                                Formatting.GRAY +
                                ")"
                        ),
                        2F,
                        context.getScaledWindowHeight() - 9 - coordinateOffset - 2 - animation.getValue(),
                        hudColor(coordinateOffset).getRGB()
                );
            }
            coordinateOffset += Managers.TEXT.height(true) + 1;
        }

        if (direction.value()) {
            String direction = getDirections();
            Managers.TEXT.drawString(getLabel(direction),
                    2,
                    context.getScaledWindowHeight() - 9 - coordinateOffset - 2 - animation.getValue(),
                    hudColor(coordinateOffset).getRGB()
            );
        }

        if (chatOpened) {
            animation.run(14);
        } else {
            animation.run(0);
        }
    }

    private String getDirections() {
        String[] directions = new String[]{"South ", "South West ", "West ", "North West ", "North ", "North East ", "East ", "South East "};
        String[] axis = new String[]{"+Z", "+Z -X", "-X", "-Z -X", "-Z", "-Z +X", "+X", "+Z +X"};

        String gang = axis[MathUtils.angleDirection(MathHelper.wrapDegrees(mc.player.getYaw()), axis.length)];
        String cool = directions[MathUtils.angleDirection(MathHelper.wrapDegrees(mc.player.getYaw()), directions.length)];

        return cool + Formatting.GRAY + "[" + Formatting.WHITE + gang + Formatting.GRAY + "]";
    }

    public float getFixedArmorOffset(float percent) {
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


    private String getString(StatusEffectInstance statusEffectInstance) {
        int amplifier = statusEffectInstance.getAmplifier();
        Text name = statusEffectInstance.getEffectType().value().getName();

        return name.getString() +
                (amplifier > 0 ? (" " + (amplifier + 1) + " ") : " ") +
                Formatting.GRAY +
                PlayerUtils.getPotionDurationString(statusEffectInstance);
    }

    private Color hudColor(int y) {
        return Managers.MODULES.getModuleFromClass(Colours.class).colorMode.is("Gradient") ? Managers.MODULES.getModuleFromClass(Colours.class).getGradientColor(y) : Managers.MODULES.getModuleFromClass(Colours.class).getColor();
    }

    public String getLabel(String label) {
        if (casing.is("Lowercase")) {
            return label.toLowerCase();
        } else if (casing.is("Uppercase")) {
            return label.toUpperCase();
        } else if (casing.is("Random")) {
            char[] array = label.toCharArray();
            int chars = 0;

            while (chars < label.length()) {

                final char character;

                if (Character.isUpperCase(array[chars])) {
                    character = Character.toLowerCase(array[chars]);
                } else {
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
