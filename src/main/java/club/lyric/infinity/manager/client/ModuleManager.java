package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author vasler
 * register the modules borther
*/
public class ModuleManager implements JsonElements, IMinecraft
{
    private final Set<ModuleBase> modules = new HashSet<>();

    public void init()
    {
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
     * gets a module by string
     * @param string - string in
     * @return - corresponding module


    public ModuleBase getModuleByString(String string)
    {
        ModuleBase moduleBase = null;
        for (ModuleBase modules : getModules()) {
            if (modules.name.equalsIgnoreCase(string)) {
                moduleBase = modules;
                break;
            }
        }
        return moduleBase;
    }
    */

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





    /*
        private void add(Module module)
        {
            modules.put(module.getClass(), module);
        }
    */

    // use add or register not both

    /**
     * Allows you to register every module at once
     */
    /*
        public void register(Module... module)
        {
            Collections.addAll(modules, modules);
        }
     */

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        /*
            for (Module module : modules)
            {
                object.add(module.getName(), module.toJson());
            }
        */
        return object;
    }

    @Override
    public void fromJson(JsonElement jsonElement)
    {
        /*
            for (Module module : modules)
            {
                module.fromJson(element.getAsJsonObject().get(module.getName()));
            }
        */
    }

    @Override
    public String getFileName()
    {
        return "modules.json";
    }
}
