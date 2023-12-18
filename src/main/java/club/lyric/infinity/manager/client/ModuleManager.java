package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.render.Render2DEvent;
import club.lyric.infinity.api.event.render.Render3DEvent;
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
 * @author vasler
 * register the modules borther
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
        /*
            for (Module module : modules)
            {
                object.add(module.getName(), module.toJson());
            }
        */
        return object;
    }

    public List<ModuleBase> getModulesInCategory(Category c) {
        return this.modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
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

    /**
     * TODO: maybe put these in a diff class?? idk
     */
    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(ModuleBase::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(ModuleBase::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void onUpdate() {
        modules.stream().filter(ModuleBase::isEnabled).forEach(ModuleBase::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(ModuleBase::isEnabled).forEach(ModuleBase::onTick);
    }

    @Override
    public String getFileName()
    {
        return "modules.json";
    }
}
