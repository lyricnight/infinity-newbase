package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.Instantiated;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.HUD;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyric and valser
 * register the modules
*/

public class ModuleManager extends Instantiated implements JsonElements, IMinecraft
{
    private final Set<ModuleBase> modules = new HashSet<>();

    public void init()
    {
        register(
                new HUD()
        );
        Infinity.LOGGER.info("Initialising modules.");
    }

    /**
     * gets modules
     */
    public Set<ModuleBase> getModules()
    {
        return modules;
    }

    /**
     * gets a module
     * @param string - string to compare to
     * @return - the module it corresponds to
     */

    public ModuleBase getModuleByString(String string)
    {
        ModuleBase moduleBase = null;
        for (ModuleBase modules : getModules()) {
            if (modules.getName().equalsIgnoreCase(string)) {
                moduleBase = modules;
                break;
            }
        }
        return moduleBase;
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

    public ModuleBase getModuleByName(String name) {
        for (ModuleBase module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
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
        return this.modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
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
