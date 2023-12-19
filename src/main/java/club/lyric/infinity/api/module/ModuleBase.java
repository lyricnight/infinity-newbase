package club.lyric.infinity.api.module;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.util.Bind;
import club.lyric.infinity.api.setting.settings.BindSetting;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.client.ConfigManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import static club.lyric.infinity.Infinity.EVENT_BUS;

/**
 * @author lyric
 * module
 */

public class ModuleBase extends Instantiated implements IMinecraft, JsonElements {

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
     * category of module
     */
    public Category category;

    /**
     * enabled/disabled
     */
    public BooleanSetting enabled = new BooleanSetting("Enabled", false, "Whether to enable module or not.");

    /**
     * module's bind.
     */
    public BindSetting bind = new BindSetting("Bind", new Bind(-1), "Bind for enabling/disabling this module.");

    /**
     * whether to draw the module on the ArrayList or not.
     */
    public BooleanSetting drawn = new BooleanSetting("Drawn", true, "Whether to draw the module on the ArrayList or not.");

    public ModuleBase(String name, String description, Category category)
    {
        super();

        this.name = name;
        this.description = description;
        this.category = category;

        /**
         * you can only instantiate the same types of settings at a time, because Sets are retarded
         */
        instantiate(this, enabled, drawn);
        instantiate(this, bind);
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
        return null;
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

    public void enable() {
        this.enabled.setValue(true);
        this.onEnable();
    }

    public void disable() {
        this.enabled.setValue(false);
        this.onDisable();
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

    /**
     * Call to 'printStackTrace()' should probably be replaced by more robust logging
     * so I replaced it with more robust logging, no clue if it works
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
        if (Boolean.parseBoolean(enabled)) enable();
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

    /**
     * null convenience
     * @return if null is present
     */
    public static boolean nullCheck()
    {
        return mc.player == null || mc.world == null;
    }
}
