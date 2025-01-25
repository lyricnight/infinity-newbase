package club.lyric.infinity.api.module;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.nulls.Null;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.manager.Managers;
import lombok.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author lyric
 * module
 */

@Getter @Setter @ToString @SuppressWarnings("unused")
public class ModuleBase implements IMinecraft {

    /**
     * name
     */
    private final String name;
    /**
     * description
     */
    private final String description;
    /**
     * module id for overwrite messages
     */
    private final int id;
    /**
     * category of module
     */
    private final Category category;
    /**
     * setting list
     */
    public List<Setting> settingList = new ArrayList<>();

    /**
     * whether to show settings or not -> for GUI.
     */
    private boolean showSettings = false;

    /**
     * enabled/disabled
     */
    private boolean enabled = false;

    /**
     * module's bind.
     */
    public BindSetting bind;

    /**
     * whether to draw the module on the ArrayList or not.
     */
    public BooleanSetting drawn;

    public float animPos = -1;


    public ModuleBase(String name, String description, Category category) {
        super();

        this.name = name;
        this.description = description;
        this.category = category;

        bind = new BindSetting("Bind", -1, this);
        drawn = new BooleanSetting("Drawn", true, this);
        id = hashCode();
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onTickPre() {}

    public void onTickPost() {}

    public void onUpdate() {}

    public void onRender2D(DrawContext drawContext) {}

    public void onRender3D(MatrixStack matrixStack) {}

    public boolean isOn() { return enabled; }

    public boolean isOff() { return !enabled; }

    public boolean isDrawn() { return drawn.value(); }

    public void setEnabled(boolean enabled) {
        if (enabled)
            enable();
        else
            disable();
    }

    public void toggle() {
        setEnabled(!isOn());
    }

    protected void enable() {
        enabled = true;
        EventBus.getInstance().register(this);
        onEnable();

        if (Null.is()) return;

        if (Managers.MODULES.getModuleFromClass(Notifications.class).toggled.value())
            Managers.MESSAGES.sendOverwriteMessage(Formatting.WHITE + getName() + " was " + Formatting.RESET + "enabled.", id, true);
    }

    protected void disable() {
        enabled = false;
        onDisable();
        EventBus.getInstance().unregister(this);

        if (Null.is()) return;

        if (Managers.MODULES.getModuleFromClass(Notifications.class).toggled.value())
            Managers.MESSAGES.sendOverwriteMessage(Formatting.WHITE + getName() + " was " + Formatting.RESET + "disabled.", id, true);
    }

    public int getBind() { return this.bind.getCode(); }

    public void setBind(int key) { this.bind.setCode(key); }

    protected void send(Packet<?> packet) {
        if (mc.getNetworkHandler() == null) return;

        mc.getNetworkHandler().sendPacket(packet);
    }

    protected void sendSeq(SequencedPacketCreator packetCreator) {
        if (mc.getNetworkHandler() == null) return;

        PendingUpdateManager sequence = mc.world.getPendingUpdateManager().incrementSequence();
        Packet<?> packet = packetCreator.predict(sequence.getSequence());

        mc.getNetworkHandler().sendPacket(packet);

        sequence.close();
    }

    protected void sendUnsafe(Packet<?> packet) { mc.getNetworkHandler().sendPacket(packet); }

    public String moduleInformation() {
        return "";
    }

    public void addSettings(Setting... settings) {
        settingList.addAll(Arrays.asList(settings));
        settingList.sort(Comparator.comparingInt(s -> s == bind ? 1 : 0));
    }

    /**
     * getter for showing settings.
     */
    public boolean showSettings() { return showSettings; }

    /**
     * toggle function for above.
     */

    public void toggleShowSettings() { showSettings = !showSettings; }

    /**
     * rendering for GUI.
     */
    public void renderSettings() {
        for (Setting setting : settingList) {
            if (setting instanceof Renderable renderable)
                renderable.render();
        }
    }


    public String getSuffix() {
        if (moduleInformation().isEmpty()) return "";

        return Formatting.GRAY + " [" + Formatting.WHITE + moduleInformation() + Formatting.GRAY + "]";
    }

    /**
     * for placement
     * @param string - type of swapmode
     * @return the enum value corresponding to string
     */

    protected SwapMode getModeFromString(String string) {
        switch (string) {
            case "Normal" -> { return SwapMode.Normal; }
            case "Silent" -> { return SwapMode.Silent; }
            case "Slot" -> { return SwapMode.Slot; }
        }
        throw new RuntimeException("No SwapMode found. Report this!");
    }

    protected enum SwapMode {
        Normal,
        Silent,
        Slot
    }
}
