package club.lyric.infinity.api.module;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.setting.RenderableSetting;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.*;
import club.lyric.infinity.api.setting.settings.util.Bind;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.api.util.client.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.manager.Managers;
import club.lyric.infinity.manager.client.ConfigManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyric
 * module
 */

@SuppressWarnings({"rawtypes", "unused"})
public class ModuleBase implements IMinecraft, JsonElements {

    /**
     * name
     */
    private final String name;
    /**
     * categoryCounting
     */
    public static int categoryCount;
    /**
     * description
     */
    private final String description;
    /**
     * module's animation factor for HUD
     */
    public float factor = 0.0f;

    /**
     * module id for overwrite messages
     */

    private final int id;

    /**
     * category of module
     */
    public Category category;

    /**
     * setting list
     */
    public List<Setting<?>> settingList = new ArrayList<>();

    /**
     * whether to show settings or not.
     */
    private boolean showSettings;

    /**
     * enabled/disabled
     */
    public BooleanSetting enabled;

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

        enabled = createBool(new BooleanSetting("Enabled", false, "Whether to enable module or not."));
        bind = createBind(new BindSetting("Bind", new Bind(-1), "Bind for enabling/disabling this module."));
        drawn = createBool(new BooleanSetting("Drawn", true, "Whether to draw the module on the ArrayList or not."));
        id = hashCode();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick() {
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
        return enabled.getValue();
    }

    public boolean isOff() {
        return !enabled.getValue();
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

    private void enable() {
        enabled.setValue(true);
        EventBus.getInstance().register(this);
        this.onEnable();
        if (Managers.MODULES.getModuleFromClass(Notifications.class).enable.getValue()) {
            ChatUtils.sendOverwriteMessageNoTag(Formatting.BOLD + getName() + " has been " + Formatting.GREEN + "enabled.", id);
        }
    }

    private void disable() {
        enabled.setValue(false);
        this.onDisable();
        EventBus.getInstance().unregister(this);
        if (Managers.MODULES.getModuleFromClass(Notifications.class).disable.getValue()) {
            ChatUtils.sendOverwriteMessageNoTag(Formatting.BOLD + getName() + " has been " + Formatting.RED + "disabled.", id);
        }
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    protected void send(Packet<?> packet) {
        if (mc.getNetworkHandler() == null) return;

        mc.getNetworkHandler().sendPacket(packet);
    }

    /**
     * setting constructors
     */
    public BooleanSetting createBool(BooleanSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public NumberSetting createNumber(NumberSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public BindSetting createBind(BindSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public StringSetting createString(StringSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public EnumSetting createEnum(EnumSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public ColorSetting createColor(ColorSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public BooleanRainbowSetting createBoolRainbow(BooleanRainbowSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    public ColorSliderSetting createColorSlider(ColorSliderSetting setting)
    {
        setting.setModule(this);
        this.settingList.add(setting);
        return setting;
    }

    /**
     * config methods
     */

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Setting<?> setting : getSettings()) {
            try {
                if (setting.getValue() instanceof Bind) {
                    object.addProperty(setting.getName(), bind.getKey());
                } else {
                    object.addProperty(setting.getName(), setting.getValueAsString());
                }
            } catch (Throwable e) {
                Infinity.LOGGER.atError();
            }
        }
        return object;
    }

    @Override
    public void fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        String enabled = object.get("Enabled").getAsString();
        if (Boolean.parseBoolean(enabled)) setEnabled(true);
        for (Setting<?> setting : getSettings()) {
            try {
                ConfigManager.setValueFromJson(setting, object.get(setting.getName()));
            } catch (Throwable throwable) {
                Infinity.LOGGER.atError();
            }
        }
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
    public List<Setting<?>> getSettings()
    {
        return settingList;
    }

    public boolean showSettings() {
        return showSettings;
    }

    public void toggleShowSettings() {
        this.showSettings = !this.showSettings;
    }

    /**
     * null convenience
     * @return if null is present
     */
    public static boolean nullCheck()
    {
        return mc.player == null || mc.world == null;
    }

    public void renderSettings() {
        for (Setting setting : settingList) {
            if (setting instanceof RenderableSetting renderableSetting) {
                renderableSetting.render();
            }
        }
    }
}
