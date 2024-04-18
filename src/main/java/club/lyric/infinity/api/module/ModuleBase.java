package club.lyric.infinity.api.module;

import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.setting.Renderable;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.Format;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author lyric
 * module
 */

@SuppressWarnings({"unused"})
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
     * whether to show settings or not.
     */
    private boolean showSettings;

    /**
     * enabled/disabled
     */
    private boolean enabled;

    /**
     * module's bind.
     */
    public BindSetting bind;

    /**
     * whether to draw the module on the ArrayList or not.
     */
    public BooleanSetting drawn;

    public ModuleBase(String name, String description, Category category)
    {
        super();

        this.name = name;
        this.description = description;
        this.category = category;
        this.showSettings = false;

        bind = new BindSetting("Bind", -1, this);
        drawn = new BooleanSetting("Drawn", true,this);
        id = hashCode();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTickPre() {
    }

    public void onTickPost() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public String getDisplayInfo() {
        return "";
    }

    public boolean isOn() {
        return enabled;
    }

    public boolean isOff() {
        return !enabled;
    }

    public boolean isDrawn() {
        return drawn.value();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void toggle()
    {
        this.setEnabled(!this.isOn());
    }

    @SuppressWarnings("ConstantConditions")
    protected void enable() {
        enabled = true;
        EventBus.getInstance().register(this);
        this.onEnable();
        ChatUtils.sendOverwriteMessageColored(Formatting.WHITE + getName() + " was" +  Formatting.RESET + " enabled.", id);
    }

    @SuppressWarnings("ConstantConditions")
    protected void disable() {
        enabled = false;
        this.onDisable();
        EventBus.getInstance().unregister(this);
        ChatUtils.sendOverwriteMessageColored(Formatting.WHITE + getName() + " was" + Formatting.RESET + " disabled.", id);
    }

    public Category getCategory() {
        return this.category;
    }

    public int getBind() {
        return this.bind.getCode();
    }

    public void setBind(int key) {
        this.bind.setCode(key);
    }

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

    protected void sendUnsafe(Packet<?> packet)
    {
        mc.getNetworkHandler().sendPacket(packet);
    }

    public String moduleInformation()
    {
        return "";
    }

    public void addSettings(Setting... settings) {
        this.settingList.addAll(Arrays.asList(settings));
        this.settingList.sort(Comparator.comparingInt(s -> s == bind ? 1 : 0));
    }

    /**
     * name
     * @return module name
     */

    public String getName()
    {
        return name;
    }

    /**
     * gets description
     * @return above
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * returns the class's settings list.
     * @return above
     */
    public List<Setting> getSettings()
    {
        return settingList;
    }

    /**
     * gui render functions
     */

    public boolean showSettings() {
        return showSettings;
    }

    public void toggleShowSettings() {
        this.showSettings = !this.showSettings;
    }

    public void renderSettings() {
        for (Setting setting : settingList) {
            if (setting instanceof Renderable renderable) {
                renderable.render();
            }
        }
    }

    /**
     * null convenience
     * @return if null is present
     */
    public static boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
