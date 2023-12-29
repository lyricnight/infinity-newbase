package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.AntiCheat;
import club.lyric.infinity.impl.modules.client.DiscordRPC;
import club.lyric.infinity.impl.modules.client.HUD;
import club.lyric.infinity.impl.modules.client.Notifications;
import club.lyric.infinity.impl.modules.exploit.HitboxDesync;
import club.lyric.infinity.impl.modules.movement.NoJumpDelay;
import club.lyric.infinity.impl.modules.player.YawLock;
import club.lyric.infinity.impl.modules.render.Chat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyric and valser
 * register the modules
*/

public class ModuleManager implements JsonElements, IMinecraft
{
    private final List<ModuleBase> modules = new ArrayList<>();

    public void init()
    {
        register(
                new AntiCheat(),
                new DiscordRPC(),
                new Chat(),
                new HUD(),
                new NoJumpDelay(),
                new HitboxDesync(),
                new Notifications(),
                new YawLock()
        );
        Infinity.LOGGER.info("Initialising modules.");
    }

    /**
     * gets modules
     */
    public List<ModuleBase> getModules()
    {
        return modules;
    }

    /**
     * gets a module from a class.
     * @param clazz - class in
     * @return - the corresponding module
     * @param <T> - type parameter
     */

    public <T extends ModuleBase> T getModuleFromClass(Class<T> clazz)
    {
        for (ModuleBase moduleBase : getModules())
        {
            if(clazz.isInstance(moduleBase))
            {
                return (T) moduleBase;
            }
        }
        return null;
    }

    /**
     * Allows you to register every module at once
     */
    public void register(ModuleBase... module)
    {
        Collections.addAll(modules, module);
    }


    /**
     * for toggle command
     * @param name - name of module in
     * @return relevant module
     */
    public ModuleBase getModuleByName(String name) {
        for (ModuleBase module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        Infinity.LOGGER.info("getModuleByName() returned null! Value passed:" + name);
        Infinity.LOGGER.info("Available values:" + modules);
        return null;
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        for (ModuleBase module : modules)
        {
            object.add(module.getName(), module.toJson());
        }

        return object;
    }

    public List<ModuleBase> getModulesInCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    @Override
    public void fromJson(JsonElement jsonElement)
    {
        for (ModuleBase module : modules)
        {
            module.fromJson(jsonElement.getAsJsonObject().get(module.getName()));
        }
    }

    @Override
    public String getFileName()
    {
        return "modules.json";
    }
}
