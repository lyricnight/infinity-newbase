package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.config.JsonElements;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author vasler
 * register the modules borther
 */
// uncomment this shit when u add module shit
public class ModuleManager implements JsonElements, IMinecraft
{

    public void init()
    {
        Infinity.LOGGER.info("Initialising modules.");
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
